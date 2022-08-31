package ramzanlabs.imessage.websocket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ramzanlabs.imessage.message.Message;
import ramzanlabs.imessage.user.User;

import java.util.Set;

@Service
public class ChatWebSocketUserNotificationService {
    public static final String WS_CHAT_DESTINATION = "/chat";


    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public ChatWebSocketUserNotificationService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void notifyUsersWithMessage(Message message) {
        /*
        System.out.println("notify user with message" + message);
        Set<User> users = message.getDiscussion().getUsers();
        for (User user: users) {
            simpMessagingTemplate.convertAndSendToUser(user.getName(), WS_CHAT_DESTINATION, message);
        }*/
        System.out.println("before sending to the user");
        simpMessagingTemplate.convertAndSendToUser("ramzi", WS_CHAT_DESTINATION, message);
        System.out.println("after sending to the user");
    }
}
