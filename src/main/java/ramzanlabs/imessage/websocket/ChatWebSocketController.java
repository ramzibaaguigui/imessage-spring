package ramzanlabs.imessage.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import ramzanlabs.imessage.headers.Headers;
import ramzanlabs.imessage.user.User;
import ramzanlabs.imessage.websocket.payload.SendMessagePayload;
import ramzanlabs.imessage.websocket.payload.StompMessage;

import java.security.Principal;

@Controller
public class ChatWebSocketController {


    private ChatWebSocketService chatWebSocketService;
    private ChatWebSocketUserNotificationService chatWebSocketUserNotificationService;
    private WebSocketAuthenticationPool webSocketAuthenticationPool;

    @Autowired
    public ChatWebSocketController(ChatWebSocketService chatWebSocketService,
                                   ChatWebSocketUserNotificationService chatWebSocketUserNotificationService,
                                   WebSocketAuthenticationPool webSocketAuthenticationPool) {
        this.chatWebSocketService = chatWebSocketService;
        this.chatWebSocketUserNotificationService = chatWebSocketUserNotificationService;
        this.webSocketAuthenticationPool = webSocketAuthenticationPool;
    }

    @MessageMapping("/sendMessage")
    // this might not be needed at the actual time
    // @SendToUser("/topic/chat")
    public void sendMessage(@Payload StompMessage<SendMessagePayload> stompMessage, Principal principal) throws JsonProcessingException, NoSuchMethodException {
        System.out.println("called message mapping");
        System.out.println();
        User user = (User) principal;
        System.out.println(user);
        System.out.println(stompMessage.getStompCommand());
        System.out.println(stompMessage.getPayload());
        System.out.println("auth header: " + stompMessage.getHeader(Headers.USER_AUTH_TOKEN));
        // fixed, the next staff is to look for the presend and the auth staff...

    }


}
