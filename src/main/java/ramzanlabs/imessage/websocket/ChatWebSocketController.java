package ramzanlabs.imessage.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import ramzanlabs.imessage.message.Message;
import ramzanlabs.imessage.user.User;
import ramzanlabs.imessage.websocket.payload.SendMessagePayload;

import java.security.Principal;

@Controller
public class ChatWebSocketController {


    @Autowired
    private ChatWebSocketService chatWebSocketService;
    @Autowired
    private ChatWebSocketUserNotificationService chatWebSocketUserNotificationService;

    @MessageMapping("/sendMessage")
    // this might not be needed at the actual time
    // @SendToUser("/topic/chat")
    public void sendMessage(@Payload SendMessagePayload sendMessagePayload, Principal principal) {
        User user = (User) principal;
        Message message = chatWebSocketService.sendMessage(sendMessagePayload, user);
        chatWebSocketUserNotificationService.notifyUsersWithMessage(message);
    }


}
