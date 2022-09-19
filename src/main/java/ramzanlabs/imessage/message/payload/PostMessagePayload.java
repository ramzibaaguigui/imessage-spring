package ramzanlabs.imessage.message.payload;


import lombok.Getter;

@Getter
public class PostMessagePayload {
    private String content;
    private Long discussionId;
}
