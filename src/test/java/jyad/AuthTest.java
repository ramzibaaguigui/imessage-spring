package jyad;

import jyad.user.auth.AuthTokenGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuthTest {
    @Autowired
    AuthTokenGenerator authTokenGenerator;

    @Test
    public void testToken() {
        System.out.println(authTokenGenerator.generateRandomAuthToken());
    }
}

