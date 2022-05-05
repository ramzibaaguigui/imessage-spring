package jyad;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class PasswordEncoderTest {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Test
    public void testPasswordEncoder() {
        System.out.println("Hello world this is the age of the machine");
        for (int i = 0; i < 10; i++) {

            System.out.println(i);
            System.out.println(passwordEncoder.encode("password"));
        }

    }
}
