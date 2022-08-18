package ramzanlabs.imessage.user.auth;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class UserAuthPool {
    private HashMap<String, Authentication> authPool = new HashMap<>();

    public Authentication validateAuthentication(String authToken) {
        return authPool.get(authToken);
    }

    public boolean storeAuthentication(Authentication authentication) {
        if (authentication == null) {
            return false;
        }
        authPool.put(authentication.getName(), authentication);
        return true;
    }
}
