package ramzanlabs.imessage.websocket.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ramzanlabs.imessage.user.User;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReceivedMessagePayload {
    private User sentBy;
    private String content;
}
