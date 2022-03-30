package jyad.user.auth;

import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Component;

@Component
public class AuthTokenGenerator {

    private static final int TOKEN_LENGTH = 64;

    public String generateRandomAuthToken() {
        return RandomString.make(TOKEN_LENGTH);
    }
}
