package ramzanlabs.imessage.user.auth;

import ramzanlabs.imessage.user.auth.exception.UsernamePasswordNotFoundException;
import ramzanlabs.imessage.user.auth.payload.UserLogoutRequestPayload;
import ramzanlabs.imessage.user.auth.payload.UserAuthRequestPayload;
import ramzanlabs.imessage.user.auth.utility.AuthHeaderManipulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@CrossOrigin(origins = "*")
public class UserAuthController {

    private final UserAuthService userAuthService;
    private final AuthHeaderManipulator headerManipulator;

    @Autowired
    public UserAuthController(UserAuthService userAuthService, AuthHeaderManipulator authHeaderManipulator) {
        this.userAuthService = userAuthService;
        this.headerManipulator = authHeaderManipulator;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody UserAuthRequestPayload authPayload, @RequestHeader HttpHeaders request) {

        try {
            UserAuth userAuth = userAuthService.authenticate(authPayload);

            ResponseEntity<?> response = ResponseEntity.ok(userAuth);
            headerManipulator.addAuthTokenHeader(response, userAuth);
            return response;


        } catch (UsernamePasswordNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .build();

        }
    }


    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody UserLogoutRequestPayload logoutRequestPayload) {
        // TOOD: todo
        boolean logoutSuccess = userAuthService.logout(logoutRequestPayload.getToken(), logoutRequestPayload.getTokenIssuedAt());
        if (logoutSuccess) {
            return ResponseEntity.ok("LOGOUT_SUCCESS");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
