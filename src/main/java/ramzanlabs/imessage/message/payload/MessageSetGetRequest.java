package ramzanlabs.imessage.message.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class MessageSetGetRequest {
    @JsonProperty("discussion_id")
    private Long discussionId;

    @JsonProperty("sent_before")
    private Date sentBefore;

    @JsonProperty("limit_message_count")
    private int limitMessageCount;

    public Long getDiscussionId() {
        return discussionId;
    }

    public Date getSentBefore() {
        return sentBefore;
    }

    public int getLimitMessageCount() {
        return limitMessageCount;
    }

    public static MessageSetGetRequest create() {
        return new MessageSetGetRequest();
    }

    public MessageSetGetRequest limitMessages(int limitMessageCount) {
        this.limitMessageCount = limitMessageCount;
        return this;
    }

    public MessageSetGetRequest discussionId(Long discussionId) {
        this.discussionId = discussionId;
        return this;
    }

    public MessageSetGetRequest sentBefore(Date sentBefore) {
        this.sentBefore = sentBefore;
        return this;
    }
}
