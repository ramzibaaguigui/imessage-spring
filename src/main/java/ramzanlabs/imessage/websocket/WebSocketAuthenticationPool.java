package ramzanlabs.imessage.websocket;

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
        if (simpSessionId == null || authentication == null) {
            return false;
        }
        this.authPool.put(simpSessionId, authentication);
        return true;
    }



}
