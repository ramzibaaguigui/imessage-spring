package jyad.message.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class MessagePayload {
    private Long id;
    private String content;

    @JsonProperty("sent_at")
    private Date sentAt;

    @JsonProperty("updated_at")
    private Date updatedAt;

    @JsonProperty("sender_username")
    private String senderUsername;

    public static MessagePayload create() {
        return new MessagePayload();
    }

    public MessagePayload withId(Long id) {
        this.id = id;
        return this;
    }

    public MessagePayload withContent(String content) {
        this.content = content;
        return this;
    }

    public MessagePayload updatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public MessagePayload sentAt(Date sentAt) {
        this.sentAt = sentAt;
        return this;
    }

    public MessagePayload senderUsername(String username) {
        this.senderUsername = username;
        return this;
    }
}
