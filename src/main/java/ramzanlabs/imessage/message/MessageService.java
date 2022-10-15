package ramzanlabs.imessage.message;

import com.mysql.cj.util.TimeUtil;
import org.springframework.http.ResponseEntity;
import ramzanlabs.imessage.message.exception.*;
import ramzanlabs.imessage.utils.TimeUtils;
import ramzanlabs.imessage.discussion.Discussion;
import ramzanlabs.imessage.discussion.DiscussionRepository;
import ramzanlabs.imessage.message.payload.MessageSetGetRequest;
import ramzanlabs.imessage.message.payload.MessageSetPostRequest;
import ramzanlabs.imessage.user.User;
import ramzanlabs.imessage.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ramzanlabs.imessage.websocket.payload.SendMessagePayload;

import java.time.Instant;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final TimeUtils time;
    private final UserRepository userRepository;
    private final DiscussionRepository discussionRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(TimeUtils time, UserRepository userRepository, DiscussionRepository discussionRepository, MessageRepository messageRepository) {
        this.time = time;
        this.userRepository = userRepository;
        this.discussionRepository = discussionRepository;
        this.messageRepository = messageRepository;
    }


    public boolean deleteMessage(Long messageId) {
        Optional<Message> message = messageRepository.getMessageById(messageId);
        if (message.isPresent()) {
            messageRepository.delete(message.get());
            return true;
        }
        return false;
    }

    public Message updateMessage(Long messageId, String content) {
        Optional<Message> message = messageRepository.getMessageById(messageId);
        if (message.isPresent()) {
            message.get().setContent(content);
            return messageRepository.save(message.get());
        }
        return null;
    }

    public Message updateMessage(User sender, Long discussionId, Long messageId, String newContent)
            throws Throwable, MessageNotFoundException, MessageNotInDiscussionException, MessageNotSentByCurrentUserException {
        Discussion discussion = discussionRepository.getDiscussionById(discussionId).orElseThrow(new Supplier<Throwable>() {
            @Override
            public Throwable get() {
                return new DiscussionNotFoundException("Cannot find discussion " + discussionId);
            }
        });

        Message message = messageRepository.getMessageById(messageId).orElseThrow(new Supplier<Throwable>() {
            @Override
            public Throwable get() {
                return new MessageNotFoundException("Cannot find message " + messageId + " in discussion " + discussionId);
            }
        });

        if (!message.isInDiscussion(discussion)){
            throw new MessageNotInDiscussionException("Cannot find message " + messageId + " in discussion " + discussionId);
        }

        if (!message.getSender().getId().equals(sender.getId())) {
            throw new MessageNotSentByCurrentUserException(String.format("Message %d was not send by %d", messageId, sender.getId()));
        }
        message.setContent(newContent);
        return messageRepository.save(message);

    }

    public Message getMessageById(Long messageId) {
        Optional<Message> message = messageRepository.getMessageById(messageId);
        return message.orElse(null);
    }

    public List<Message> getMessageList(MessageSetGetRequest messageSetRequest) {
        Long discussionId = messageSetRequest.getDiscussionId();
        int limitMessageCount = messageSetRequest.getLimitMessageCount();
        Date newestDate = messageSetRequest.getSentBefore();

        return (List<Message>) messageRepository.findMessagesByDiscussionAndSentAtBeforeAndIsDeletedIsOrderBySentAtDesc(
                        discussionId, newestDate, false)
                .stream()
                .limit(limitMessageCount);
    }

    public List<Message> postMessages(MessageSetPostRequest messageSetPostRequest) {
        Optional<Discussion> discussion = discussionRepository.getDiscussionById(messageSetPostRequest.getDiscussionId());
        Optional<User> sender = userRepository.getUserById(messageSetPostRequest.getSenderId());
        if (discussion.isPresent() && sender.isPresent()) {

            List<Message> messagesToSend = messageSetPostRequest.getMessages();
            List<Message> sentMessages = new ArrayList<>();

            for (Message message : messagesToSend) {
                message.setDiscussion(discussion.get());
                message.setSender(sender.get());
                message.setSentAt(time.now());
                message.setUpdatedAt(time.now());
                message.setIsDeleted(false);
                message = messageRepository.save(message);
                sentMessages.add(message);
            }
            return sentMessages;
        }

        return null;
    }

    public Message sendMessage(User sender, SendMessagePayload sendMessagePayload) {
        Discussion discussion = discussionRepository.getDiscussionById(sendMessagePayload.getDiscussionId()).get();
        if (userCanSendInDiscussion(sender, discussion)) {
            var message = createMessage(sender, discussion, sendMessagePayload.getContent());
            discussion.addMessage(message);
            discussionRepository.save(discussion);
            return message;
        }
        return null;
    }

    private boolean userCanSendInDiscussion(User sender, Discussion discussion) {
        if (sender == null || discussion == null) {
            return false;
        }

        return discussion.hasUser(sender);
    }

    private Message createMessage(User sender, Discussion discussion, String content) {
        Message message = new Message();
        message.setContent(content);
        message.setSender(sender);
        message.setDiscussion(discussion);
        Date now = Date.from(Instant.now());
        message.setSentAt(now);
        message.setUpdatedAt(now);
        return message;
    }

    public Long deleteMessage(User currentUser, Long discussionId, Long messageId)
            throws Throwable, UserNotInDiscussionException, MessageNotFoundException,
            MessageNotInDiscussionException, MessageNotSentByCurrentUserException{
        Discussion discussion = discussionRepository.getDiscussionById(discussionId)
                .orElseThrow((Supplier<Throwable>) () -> new DiscussionNotFoundException("Cannot find discussion with id: " + discussionId));

        if (!discussion.hasUser(currentUser)) {
            throw new UserNotInDiscussionException(
                    String.format("The user with id %d is not a member of the discussion with id %d",
                            currentUser.getId(),
                            discussion.getId()));
        }
        Message messageToDelete = messageRepository.getMessageById(messageId).orElseThrow(new Supplier<Throwable>() {
            @Override
            public Throwable get() {
                return new MessageNotFoundException("Cannot find message with id " + messageId);
            }
        });

        if (!messageToDelete.isInDiscussion(discussion)) {
            throw new MessageNotInDiscussionException("The message with id " + messageId + " is not in the discussion with id " + discussionId);
        }

        if (!messageToDelete.isSentBy(currentUser)) {
            throw new MessageNotSentByCurrentUserException("The user " + currentUser.getId() + " cannot delete the message " + messageId);
        }
        messageRepository.delete(messageToDelete);
        return messageId;
    }

    public List<Message> getLastDiscussionMessagesBeforeMessage(Long discussionId, Long messageId) throws Throwable{

        Discussion discussion = discussionRepository.getDiscussionById(discussionId)
                .orElseThrow(new Supplier<Throwable>() {
                    @Override
                    public Throwable get() {
                        return new DiscussionNotFoundException("Cannot find discussion with id " + discussionId);
                    }
                });
        return discussion.getMessages()
                .stream()
                .filter(message -> Long.compare(message.getId(), messageId) < 0)
                .sorted((message1, message2) -> message1.getId().compareTo(message2.getId()))
                .collect(Collectors.toList());

    }
}
