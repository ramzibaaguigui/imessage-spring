package ramzanlabs.imessage.websocket.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SendMessagePayload {
    @JsonProperty("content")
    private String content;

    @JsonProperty("discussionId")
    private Long discussionId;
}
