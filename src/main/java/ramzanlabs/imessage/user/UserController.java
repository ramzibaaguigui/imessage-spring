package ramzanlabs.imessage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ramzanlabs.imessage.discussion.Discussion;
import ramzanlabs.imessage.headers.Headers;
import ramzanlabs.imessage.user.auth.UserAuthService;
import ramzanlabs.imessage.user.utils.UserValidator;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
public class UserController {
    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final UserValidator userValidator;
    private final UserAuthService userAuthService;

    @Autowired
    public UserController(UserService userService, UserValidator userValidator, UserAuthService userAuthService) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.userAuthService = userAuthService;
    }

    @PostMapping("/user/create")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        LOGGER.info("Just received a user post request ");
        LOGGER.info(user.toString());
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


    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        LOGGER.info("Recieved a request for user with id " + id);
        User user = userService.getUserById(id);
        LOGGER.info(String.format("User with id %d found", id));
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

    @GetMapping("/user/email")
    public ResponseEntity<?> getUserByEmail(@RequestParam("email") String email) {
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

    @GetMapping("user/discussions/get")
    public ResponseEntity<?> getUserDiscussions(
            @RequestHeader(Headers.USER_AUTH_TOKEN) String authToken,
            Authentication auth) {

        List<Discussion> userDiscussions = userService.getUserDiscussions(auth);
        if (Objects.nonNull(userDiscussions)) {
            return ResponseEntity.ok(userDiscussions);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("user/search")
    public ResponseEntity<?> searchUser(@RequestParam("q") String searchQuery) {
        Set<User> users = userService.searchUsers(searchQuery);
        if (users == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(users);
    }

    @GetMapping("users/all")
    public ResponseEntity<?> getAllUsers(Principal principal) {
        System.out.println("returning all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("users/contacts")
    public ResponseEntity<?> getContacts(Principal principal) {
        User user = (User) principal;
        return ResponseEntity.ok(userService.
                getAllContacts(user));
    }

}
