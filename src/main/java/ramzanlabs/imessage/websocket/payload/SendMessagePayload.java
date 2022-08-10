package ramzanlabs.imessage.websocket.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SendMessagePayload {
    private String content;
    private Long discussionId;
}
