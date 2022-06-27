package ramzanlabs.imessage.message;

import ramzanlabs.imessage.date.TimeUtils;
import ramzanlabs.imessage.discussion.Discussion;
import ramzanlabs.imessage.discussion.DiscussionRepository;
import ramzanlabs.imessage.message.payload.MessageSetGetRequest;
import ramzanlabs.imessage.message.payload.MessageSetPostRequest;
import ramzanlabs.imessage.user.User;
import ramzanlabs.imessage.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    @Autowired
    TimeUtils time;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DiscussionRepository discussionRepository;

    @Autowired
    MessageRepository messageRepository;


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

}
