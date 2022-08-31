package ramzanlabs.imessage.websocket.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SendMessagePayload {
    private static final String KEY_DISCUSSION_ID = "discussionId";
    private static final String KEY_CONTENT = "content";
    @JsonProperty("content")
    private String content;

    @JsonProperty("discussionId")
    private Long discussionId;

    public static SendMessagePayload createFromHashMap(LinkedHashMap<String, Object> hashMap) {
        SendMessagePayload sendMessagePayload = new SendMessagePayload();
        sendMessagePayload.setContent((String) hashMap.get(KEY_CONTENT));
        sendMessagePayload.setDiscussionId(Long.valueOf(String.valueOf(hashMap.get(KEY_DISCUSSION_ID))));
        return sendMessagePayload;
    }
}
