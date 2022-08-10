package ramzanlabs.imessage.discussion.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DiscussionInquiryRequestPayload {

    @JsonProperty("user_id")
    private Long userId;

    public Long getUserId() {
        return this.userId;
    }
}
