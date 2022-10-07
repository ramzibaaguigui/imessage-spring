package ramzanlabs.imessage.user.auth;

import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;

@Service
public class TokenGenerator {
    private static final int TOKEN_LENGTH = 64;

    public String generateRandomToken() {
        return RandomString.make(TOKEN_LENGTH);
    }


}
