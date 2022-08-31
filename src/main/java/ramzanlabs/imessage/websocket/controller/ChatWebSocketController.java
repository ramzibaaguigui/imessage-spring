package ramzanlabs.imessage.websocket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import ramzanlabs.imessage.headers.Headers;
import ramzanlabs.imessage.message.Message;
import ramzanlabs.imessage.user.User;
import ramzanlabs.imessage.user.auth.UserAuthPool;
import ramzanlabs.imessage.websocket.service.ChatWebSocketService;
import ramzanlabs.imessage.websocket.service.ChatWebSocketUserNotificationService;
import ramzanlabs.imessage.websocket.config.WebSocketAuthenticationPool;
import ramzanlabs.imessage.websocket.payload.SendMessagePayload;
import ramzanlabs.imessage.websocket.payload.StompMessage;

import java.util.LinkedHashMap;

@Controller
public class ChatWebSocketController {


    private final ChatWebSocketService chatWebSocketService;
    private final ChatWebSocketUserNotificationService chatWebSocketUserNotificationService;
    private final WebSocketAuthenticationPool webSocketAuthenticationPool;
    private final UserAuthPool userAuthPool;

    @Autowired
    public ChatWebSocketController(ChatWebSocketService chatWebSocketService,
                                   ChatWebSocketUserNotificationService chatWebSocketUserNotificationService,
                                   WebSocketAuthenticationPool webSocketAuthenticationPool,
                                   UserAuthPool userAuthPool) {
        this.chatWebSocketService = chatWebSocketService;
        this.chatWebSocketUserNotificationService = chatWebSocketUserNotificationService;
        this.webSocketAuthenticationPool = webSocketAuthenticationPool;
        this.userAuthPool = userAuthPool;
    }

    @MessageMapping("/sendMessage")
    // this might not be needed at the actual time
    // @SendToUser("/topic/chat")
    public void sendMessage(@Payload StompMessage<LinkedHashMap> stompMessage) throws JsonProcessingException, NoSuchMethodException {
        String authToken = stompMessage.getHeader(Headers.USER_AUTH_TOKEN);
        User sender = ((User) userAuthPool.validateAuthentication(authToken).getPrincipal());
        System.out.println(sender);
        System.out.println(stompMessage);
        System.out.println(stompMessage.getPayload());
        SendMessagePayload sendMessagePayload = SendMessagePayload.createFromHashMap(stompMessage.getPayload());
        Message sentMessage = chatWebSocketService.sendMessage(sendMessagePayload, sender);
        if (sentMessage == null) {
            return;
        }

        chatWebSocketUserNotificationService.notifyUsersWithMessage(sentMessage);
    }


}
