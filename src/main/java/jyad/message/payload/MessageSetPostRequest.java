package jyad.message.payload;

import jyad.message.Message;

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
}
