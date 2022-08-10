package ramzanlabs.imessage.websocket;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class WebSocketAuthenticationPool {
    private HashMap<String, Authentication> authPool = new HashMap<>();

    public Authentication validateAuthentication(String authToken) {
        return authPool.get(authToken);
    }

    public boolean storeAuthentication(Authentication auth) {
        if (auth == null) {
            return false;
        }
        authPool.put(auth.getName(), auth);
        return true;
    }


}
