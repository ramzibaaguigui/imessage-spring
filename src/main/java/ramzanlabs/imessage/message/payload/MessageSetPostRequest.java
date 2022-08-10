package ramzanlabs.imessage.message.payload;

import ramzanlabs.imessage.message.Message;
import ramzanlabs.imessage.websocket.exception.DiscussionNotSetException;
import ramzanlabs.imessage.websocket.exception.SenderNotSetException;

import java.util.ArrayList;
import java.util.List;

public class MessageSetPostRequest {
    private Long discussionId;
    private Long senderId;
    private List<Message> messages;

    public Long getDiscussionId() {
        return discussionId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public static MessageSetPostRequest create() {
        var request = new MessageSetPostRequest();
        request.messages = new ArrayList<>();
        return request;
    }

    public MessageSetPostRequest senderId(Long senderId) {
        this.senderId = senderId;
        return this;
    }

    public MessageSetPostRequest discussionId(Long discussionId) {
        this.discussionId = discussionId;
        return this;
    }

}
