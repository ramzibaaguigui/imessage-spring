package ramzanlabs.imessage.user.validation;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ramzanlabs.imessage.user.User;
import ramzanlabs.imessage.user.auth.UserAuth;
import ramzanlabs.imessage.user.validation.exception.EmailAlreadyValidatedException;
import ramzanlabs.imessage.user.validation.exception.NoSuchEmailValidationTokenException;

import javax.mail.MessagingException;
import java.security.Principal;

@Controller
public class EmailValidationController {


    private final EmailValidationService emailValidationService;

    @Autowired
    public EmailValidationController(EmailValidationService emailValidationService) {
        this.emailValidationService = emailValidationService;
    }

    @GetMapping("/validate/email/verify")
    public ResponseEntity<?> submitEmailValidation(@RequestParam("token") String token) {
        try {
            EmailValidation validation = emailValidationService.submitEmailValidation(token);
            return ResponseEntity.ok(validation);
        } catch (NoSuchEmailValidationTokenException exception) {
            return ResponseEntity.notFound().build();
        } catch (EmailAlreadyValidatedException exception) {
            return ResponseEntity.status(HttpStatus.IM_USED).build();
        }
    }

    @PostMapping("/validate/email/request")
    public ResponseEntity<?> requestEmailValidation(Principal principal) {
        User currentUser = ((UserAuth) principal).getAuthUser();

        if (currentUser == null) {
            return ResponseEntity.badRequest().build();
        }

        EmailValidation validation = null;
        try {
            validation = emailValidationService.requestEmailValidation(currentUser);
        } catch (MessagingException e) {
            return ResponseEntity.internalServerError().build();
        }
        if (validation == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }


}
