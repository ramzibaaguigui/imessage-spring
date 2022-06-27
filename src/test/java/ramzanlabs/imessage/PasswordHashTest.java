package ramzanlabs.imessage;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashTest {


    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void testHash() {
        String password = "password";
        String pass = "pass";
        String hash1 = passwordEncoder.encode(password);
        String hash2 = passwordEncoder.encode(password);



    }
}
