package ramzanlabs.imessage.user.validation.exception;

public class EmailAlreadyValidatedException extends Throwable {
    public EmailAlreadyValidatedException(String token) {
        super(token);
    }
}
