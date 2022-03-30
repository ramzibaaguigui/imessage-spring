package jyad.message.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class MessageSetGetRequest {
    @JsonProperty("discussion_id")
    private String discussionId;

    @JsonProperty("sent_before")
    private Date sentBefore;

    @JsonProperty("limit_message_count")
    private int limitMessageCount;

    public String getDiscussionId() {
        return discussionId;
    }

    public Date getSentBefore() {
        return sentBefore;
    }

    public int getLimitMessageCount() {
        return limitMessageCount;
    }
}
