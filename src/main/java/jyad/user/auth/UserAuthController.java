package jyad.user.auth;

import jyad.user.auth.payload.UserAuthRequestPayload;
import jyad.user.auth.payload.UserLogoutRequestPayload;
import jyad.user.auth.utility.AuthHeaderManipulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

@Controller
@CrossOrigin(origins = "*")
public class UserAuthController {

    @Autowired
    UserAuthService userAuthService;

    @Autowired
    AuthHeaderManipulator headerManipulator;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody UserAuthRequestPayload authPayload, @RequestHeader HttpHeaders request) {
        UserAuth userAuth = userAuthService.authenticate(authPayload);

        printHeaders(request);
        if (userAuth != null) {
            ResponseEntity<?> response = ResponseEntity.ok(userAuth);
            headerManipulator.addAuthTokenHeader(response, userAuth);
            return response;
        }

        // if the user auth is not valid
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .build();
    }

    private void printHeaders(HttpHeaders headers) {
        System.out.println("000000000000000000000000000000000000000000000000000000000000000000000000000000");
        Set<String> keySet = headers.keySet();
        for (String key : keySet) {
            System.out.println(key + " " + headers.get(key));
        }

        System.out.println("000000000000000000000000000000000000000000000000000000000000000000000000000000");

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
