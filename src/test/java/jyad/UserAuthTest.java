package jyad;

import jyad.user.User;
import jyad.user.utils.UserValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

@SpringBootTest
public class UserAuthTest {

    @Autowired
    UserValidator userValidator;

    @Test
    public void testUserAuth() {
        User user = new User();
        user.setUserName("ramzi_baaguigui");
        user.setPassword("password");
        User returnedUser = userValidator.validateUserAuthentication(user);
        Assertions.assertNotNull(returnedUser);

    }

    @Test
    public void testUserNonNull() {
        User user = new User() {{
            setUserName("ramzi_baaguigui");
            setPassword("password");
        }};
        if (Objects.nonNull(user.getUserName()) && Objects.nonNull(user.getPassword())) {
            System.out.println("The user is valid");
        } else {
            System.out.println("It says that the user is invalid");
        }
    }
}
