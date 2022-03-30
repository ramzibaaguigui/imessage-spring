package jyad.user;

import jyad.user.auth.UserAuthService;
import jyad.user.utils.AuthConstants;
import jyad.user.utils.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

@Controller
public class UserController {
    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Autowired
    UserValidator userValidator;


    @Autowired
    UserAuthService userAuthService;

    @PostMapping("/user/create")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (userValidator.validateUserRegistration(user) && user.passwordMeetsRequirements()) {
            LOGGER.info("user creation received, request body:\n" + user
            );
            User createdUser = userService.createUser(user);
            return ResponseEntity.ok(
                    createdUser
            );
        }
        return ResponseEntity.badRequest().body("INVALID_CREDENTIALS");
    }

    @PostMapping("/user/authenticate")
    public ResponseEntity<?> authenticateUser(@RequestBody User user,
                                              HttpServletRequest httpServletRequest,
                                              HttpServletResponse response) {
        User authUser = userValidator.validateUserAuthentication(user);
        if (authUser != null) {
            String authToken = userAuthService.generateAuthToken(user);
            Cookie authTokenCookie = new Cookie(AuthConstants.AUTH_TOKEN, authToken);
            authTokenCookie.setMaxAge(AuthConstants.DURATION_WEEK);
            response.addCookie(authTokenCookie);
            return ResponseEntity.ok(authUser);
        }
        return ResponseEntity.badRequest().body("INVALID_CREDENTIALS");
    }


    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (userService.deleteUserById(id)) {
            return ResponseEntity.ok().build();
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/user/{userId}/add/discussion/{discussionId}")
    public ResponseEntity<?> addUserToDiscussion(@PathVariable("userId") Long userId,
                                                 @PathVariable("discussionId") Long discussionId) {
        if (userService.addUserToDiscussion(userId, discussionId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/user/{userId}/update/firstName")
    public ResponseEntity<?> updateUserFirstName(@PathVariable("userId") Long userId,
                                                 @RequestParam("first_name") String firstName) {
        User updatedUser = userService.updateUserFirstName(userId, firstName);

        if (Objects.nonNull(updatedUser)) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/user/{userId}/update/lastName")
    public ResponseEntity<?> updateUserLastName(@PathVariable("userId") Long userId,
                                                @RequestParam("last_name") String lastName) {
        User updatedUser = userService.updateUserLastName(userId, lastName);

        if (Objects.nonNull(updatedUser)) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        if (Objects.nonNull(user)) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/ids")
    public ResponseEntity<?> getUsersByIsIn(@RequestBody List<Long> userIds) {
        List<User> users = userService.getUsersByIdIn(userIds);
        if (Objects.nonNull(users)) {
            return ResponseEntity.ok(users);
        }
        return ResponseEntity.notFound().build();
    }

}
