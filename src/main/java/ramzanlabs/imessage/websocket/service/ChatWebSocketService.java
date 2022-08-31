package ramzanlabs.imessage.websocket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ramzanlabs.imessage.discussion.Discussion;
import ramzanlabs.imessage.discussion.DiscussionService;
import ramzanlabs.imessage.message.Message;
import ramzanlabs.imessage.message.MessageService;
import ramzanlabs.imessage.user.User;
import ramzanlabs.imessage.websocket.payload.SendMessagePayload;

@Service
public class ChatWebSocketService {

    private final MessageService messageService;
    private final DiscussionService discussionService;

    @Autowired
    public ChatWebSocketService(MessageService messageService,
                                DiscussionService discussionService) {
        this.messageService = messageService;
        this.discussionService = discussionService;
    }

    public Message sendMessage(SendMessagePayload sendMessagePayload,
                                              @Nullable User sender) {
        if (sender == null || !validateSendMessagePayload(sendMessagePayload)) {
            return null;
        }
        System.out.println("message mapping, sending the message");
        return messageService.sendMessage(sender, sendMessagePayload);
    }

    private boolean validateSendMessagePayload(SendMessagePayload sendMessagePayload) {
        if (sendMessagePayload.getDiscussionId() == null || sendMessagePayload.getContent() == null) {
            return false;
        }
        return !sendMessagePayload.getContent().isEmpty();
    }
}
