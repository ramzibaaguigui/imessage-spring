package ramzanlabs.imessage.user.validation;

import com.mysql.cj.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ramzanlabs.imessage.date.TimeUtils;
import ramzanlabs.imessage.user.User;
import ramzanlabs.imessage.user.UserRepository;
import ramzanlabs.imessage.user.auth.TokenGenerator;
import ramzanlabs.imessage.user.validation.exception.EmailAlreadyValidatedException;
import ramzanlabs.imessage.user.validation.exception.NoSuchEmailValidationTokenException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Service
public class EmailValidationService {

    private static final String EMAIL_VERIFICATION = "Email Verification";
    private static final String PERSONAL_EMAIL = "imessagewhatsapp@outlook.com";
    private static final String SLUG_VALIDATE_EMAIL = "/validate/email/verify";
    private final TokenGenerator validationTokenGenerator;
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final Environment environment;
    private final EmailValidationRepository emailValidationRepository;

    private final TimeUtils time;

    @Autowired
    public EmailValidationService(TokenGenerator validationTokenGenerator, JavaMailSender javaMailSender, Environment environment, UserRepository userRepository, EmailValidationRepository emailValidationRepository, TimeUtils time) {
        this.validationTokenGenerator = validationTokenGenerator;
        this.javaMailSender = javaMailSender;
        this.environment = environment;
        this.userRepository = userRepository;
        this.emailValidationRepository = emailValidationRepository;
        this.time = time;
    }

    private void sendValidationEmail(String emailAddress, User user, String validationToken) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setSubject(EMAIL_VERIFICATION + validationToken
        );
        helper.setFrom(PERSONAL_EMAIL);
        helper.setTo(emailAddress);
        message.setContent(formatMessageContent(validationToken, user), "text/html");
        javaMailSender.send(message);
    }

    private String formatMessageContent(String validationToken, User user) {
        return String.format(
                "<h6>Hello %s, this is your validation link "
                        + "<a href=\"%s\">,/h6>",
                user.getUserName(),
                generateEmailValidationLinkWithToken(validationToken));
    }

    private String generateEmailValidationToken() {
        return validationTokenGenerator.generateRandomToken();
    }


/*
    private void sendTestMail() throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("imessagewhatsapp@outlook.com");
        helper.setTo("ramorimo1233@gmail.com");
        helper.setSubject("Hello, World223332!");
        message.setContent("<h1>Hello world <h1>", "text/html");
        javaMailSender.send(message);
    }
*/

    private String generateEmailValidationLinkWithToken(String token) {
        return String.format("http://%s:%s/%s?token=%s", environment.getProperty("server.host"), environment.getProperty("server.port"), SLUG_VALIDATE_EMAIL, token);
    }

    public EmailValidation requestEmailValidation(User user) throws MessagingException {
        EmailValidation validation = constructEmailValidationForUser(user);
        sendValidationEmail(validation.getEmail(), user, validation.getValidationToken());
        return validation;
    }

    private EmailValidation constructEmailValidationForUser(User user) {
        Date now = time.now();
        EmailValidation validation = new EmailValidation();
        validation.setUser(user);
        validation.setEmail(user.getEmail());
        validation.setValidationToken(generateEmailValidationToken());
        validation.setCreatedAt(now);
        validation.setIsValidated(false);
        validation.setValidatedAt(now);
        return emailValidationRepository.save(validation);

    }

    public EmailValidation submitEmailValidation(String validationToken) throws NoSuchEmailValidationTokenException, EmailAlreadyValidatedException {
        EmailValidation validation = emailValidationRepository.findAll().stream().filter(emailValidation -> emailValidation.getValidationToken().equals(validationToken)).findFirst().orElse(null);
        if (validation == null) {
            throw new NoSuchEmailValidationTokenException(String.format("Token %s does not exist", validationToken));
        }

        if (validation.getIsValidated()) {
            throw new EmailAlreadyValidatedException(String.format("Token \"%s\" is expired"));
        }

        validation.setIsValidated(true);
        validation.setValidatedAt(time.now());
        return emailValidationRepository.save(validation);
    }


}
