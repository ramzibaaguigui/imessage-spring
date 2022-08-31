package ramzanlabs.imessage.websocket.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class WebSocketAuthenticationPool {
    private HashMap<String, Authentication> authPool = new HashMap<>();

    public Authentication validateAuthentication(String simpSessionId) {
        if (simpSessionId == null) {
            return null;
        }
        return authPool.get(simpSessionId);
    }

    public boolean storeAuthentication(String simpSessionId, Authentication authentication) {
        System.out.println("store auth in pool");
        if (simpSessionId == null || authentication == null) {
            return false;
        }
        this.authPool.put(simpSessionId, authentication);
        System.out.println(authPool);
        return true;
    }



}
