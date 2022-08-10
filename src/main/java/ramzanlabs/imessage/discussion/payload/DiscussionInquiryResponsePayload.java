package ramzanlabs.imessage.discussion.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import ramzanlabs.imessage.discussion.Discussion;

public class DiscussionInquiryResponsePayload {

    @JsonProperty("discussion")
    private Discussion discussion;

    @JsonProperty("discussion_is_new")
    private Boolean discussionIsNew;

    public static DiscussionInquiryResponsePayload create(Discussion discussion) {
        DiscussionInquiryResponsePayload payload = new DiscussionInquiryResponsePayload();
        payload.discussion = discussion;
        payload.discussionIsNew = discussion.discussionIsNew();
        return payload;
    }


}
