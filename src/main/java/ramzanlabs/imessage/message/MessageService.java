package ramzanlabs.imessage.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ramzanlabs.imessage.date.TimeUtils;
import ramzanlabs.imessage.discussion.Discussion;
import ramzanlabs.imessage.discussion.DiscussionRepository;
import ramzanlabs.imessage.message.payload.MessageSetGetRequest;
import ramzanlabs.imessage.message.payload.MessageSetPostRequest;
import ramzanlabs.imessage.user.User;
import ramzanlabs.imessage.user.UserRepository;
import ramzanlabs.imessage.websocket.payload.SendMessagePayload;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final TimeUtils time;
    private final UserRepository userRepository;
    private final DiscussionRepository discussionRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(TimeUtils time,
                          UserRepository userRepository,
                          DiscussionRepository discussionRepository,
                          MessageRepository messageRepository) {
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

    public Message getMessageById(Long messageId) {
        Optional<Message> message = messageRepository.getMessageById(messageId);
        return message.orElse(null);
    }

    public List<Message> getMessageList(MessageSetGetRequest messageSetRequest) {
        Long discussionId = messageSetRequest.getDiscussionId();
        int limitMessageCount = messageSetRequest.getLimitMessageCount();
        Date newestDate = messageSetRequest.getSentBefore();
        // todo: the problem is here
        // TODO: the service is not returning the discussion messages
        // todo: YOU SHOULD CONSIDER FINDING THE PROBLEM IN IT
        return messageRepository.findMessagesByDiscussionAndSentAtBeforeAndIsDeletedIsOrderBySentAtDesc(
                        discussionId, newestDate, false)
                .stream()
                .limit(limitMessageCount)
                .collect(Collectors.toList());
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

}
