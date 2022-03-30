package jyad;

import jyad.user.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TailwindSpringBootApplicationTests {

    @Autowired
    UserRepository userRepository;

    @Test
    public void contextLoads() {
        boolean a = userRepository.existsByEmailOrUserName("", "hamidou");
        boolean b = userRepository.existsByEmailOrUserName("youseff@esi-sba.dz", "hellofffd");
        Assertions.assertTrue(a);
        Assertions.assertTrue(b);

        boolean c = userRepository.existsByEmailOrUserName("dummy email", "dummay name");
        Assertions.assertFalse(c);
    }

}
