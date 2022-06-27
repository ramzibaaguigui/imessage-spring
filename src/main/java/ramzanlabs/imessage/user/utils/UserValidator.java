package ramzanlabs.imessage.user.utils;

import ramzanlabs.imessage.user.User;
import ramzanlabs.imessage.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserValidator {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;


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
//        if (Objects.nonNull(user.getUserName()) && Objects.nonNull(user.getPassword())) {
        System.out.println("here are the encoded passwords");

        System.out.println(passwordEncoder.encode("password"));
        System.out.println(passwordEncoder.encode("password"));

        if (user.getUserName() != null && user.getPassword() != null) {
            // trimming username
            user.setUserName(user.getUserName().trim());

            // encrypting password
            // user.setPassword(passwordEncoder.encode(user.getPassword()));

            System.out.println(user);
            User returnedUser = getUserByUserNameAndPassword(user.getUserName(), user.getPassword());
            System.out.println("Here is the official thing");

            System.out.println(returnedUser);
            return returnedUser;
        } else {
            System.out.println("It says that the user is null");
        }
        return null;

    }

    private User findUserByUsernameAndPassword(String username, String password) {
        List<User> users = userRepository.findAll()
                .stream()
                .filter(user -> user.getUserName().equals(username) && user.getPassword().equals(password))
                .collect(Collectors.toList());
        if (users.size() == 1) {
            return users.get(0);
        }
        return null;
    }

    private User getUserByUserNameAndPassword(String username, String password) {
        // Optional<User> user = userRepository.findFirstByUserNameAndPassword(username, password);
        //return user.orElse(null);\
        System.out.printf("Looking for username %s and password %s%n", username, password);
        Optional<User> returnedUser = userRepository.findAll()
                .stream()
                .filter(user -> ((user.getUserName().equals(username))) && passwordEncoder.matches(password, user.getPassword()))
                .findFirst();
        return returnedUser.orElse(null);
    }


}
