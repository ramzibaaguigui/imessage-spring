package ramzanlabs.imessage.discussion.payload;

import ramzanlabs.imessage.user.User;

import java.util.Date;
import java.util.List;

public class DiscussionPayload {
    private Long discussionId;
    private List<Long> userIds;

    private User createdBy;

    private Date createdAt;


}
