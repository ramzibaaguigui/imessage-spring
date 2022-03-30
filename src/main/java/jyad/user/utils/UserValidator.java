package jyad.user.utils;

import jyad.user.User;
import jyad.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserValidator {

    @Autowired
    UserRepository userRepository;


    public boolean validateUserRegistration(User user) {
        try {
            user.trim();
            boolean userExists = userRepository.existsByEmailOrUserName(user.getEmail(), user.getUserName());
            return !userExists;
        } catch (Exception exception) {
            return false;
        }
    }

    public User validateUserAuthentication(User user) {
        if (Objects.nonNull(user.getUserName()) && Objects.nonNull(user.getPassword())) {
            // trimming username
            user.setUserName(user.getUserName().trim());
            return userRepository.getFirstByUserNameAndPassword(user.getUserName(), user.getPassword()).orElse(null);
        }
        return null;
    }


}
