package jyad.user.auth;

import jyad.user.User;
import jyad.user.UserController;
import jyad.user.utils.AuthConstants;
import jyad.user.utils.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@CrossOrigin(origins = "*")
public class AuthController {
    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserValidator userValidator;

    @Autowired
    UserAuthService userAuthService;

    @PostMapping("/user/authenticate")
    public ResponseEntity<?> authenticateUser(@RequestBody User user,
                                              HttpServletRequest httpServletRequest,
                                              HttpServletResponse response) {
        LOGGER.info("Received an authentication request");
        System.out.println(user);
        // this is returning null
        User authUser = userValidator.validateUserAuthentication(user);
        if (authUser != null) {
            LOGGER.trace(authUser.toString());
            // String authToken = userAuthService.generateAuthToken(user);
            // Cookie authTokenCookie = new Cookie(AuthConstants.AUTH_TOKEN, authToken);
            // authTokenCookie.setMaxAge(AuthConstants.DURATION_WEEK);
            // response.addCookie(authTokenCookie);
            return ResponseEntity.ok(authUser);
        }
        LOGGER.debug("User is null");
        return ResponseEntity.badRequest().body("INVALID_CREDENTIALS");
    }

    @PostMapping
    public ResponseEntity<?> logoutUser(Authentication authentication) {

        return null;
        //todo: implement the logout functionality in the web
    }
}
