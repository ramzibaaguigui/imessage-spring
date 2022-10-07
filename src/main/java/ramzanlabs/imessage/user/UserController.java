package ramzanlabs.imessage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ramzanlabs.imessage.discussion.Discussion;
import ramzanlabs.imessage.headers.Headers;
import ramzanlabs.imessage.user.auth.UserAuth;
import ramzanlabs.imessage.user.auth.UserAuthService;
import ramzanlabs.imessage.user.exception.CannotCreateImageFolderException;
import ramzanlabs.imessage.user.exception.FileTypeNotImageException;
import ramzanlabs.imessage.user.exception.MaximumImageSizeExceededException;
import ramzanlabs.imessage.user.exception.NullSearchQueryException;
import ramzanlabs.imessage.user.file.ImageUploadService;
import ramzanlabs.imessage.user.utils.UserValidator;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
public class UserController {
    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final UserValidator userValidator;


    private final ImageUploadService imageUploadService;

    @Autowired
    public UserController(UserService userService, UserValidator userValidator,
                          ImageUploadService imageUploadService) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.imageUploadService = imageUploadService;
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
        try {
            Set<User> users = userService.searchUsers(searchQuery);
            return ResponseEntity.ok(users);
        } catch (NullSearchQueryException exception) {
            return ResponseEntity.badRequest().build();
        }
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

    @PostMapping("/user/update/profile/image")
    public ResponseEntity<?> updateProfileImage(Principal principal, @RequestPart(value = "image_file") MultipartFile imageFile) {
        User currentUser = ((UserAuth) principal).getAuthUser();
        System.out.println("the currently logged user is");
        System.out.println(currentUser);
        try {
            User updatedUser = imageUploadService.updateProfileImage(currentUser, imageFile);
            return ResponseEntity.ok(updatedUser);
        } catch (FileTypeNotImageException exception) {
            System.out.println("file type not image");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        } catch (MaximumImageSizeExceededException exception) {
            System.out.println("payload too large ");
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build();
        } catch (IOException exception) {
            LOGGER.debug(exception.getMessage());
            System.out.println("io exception");
            return ResponseEntity.internalServerError().build();
        } catch (CannotCreateImageFolderException exception) {
            System.out.println("cannot create folder exception");
            return ResponseEntity.internalServerError().build();

        }

    }

}
