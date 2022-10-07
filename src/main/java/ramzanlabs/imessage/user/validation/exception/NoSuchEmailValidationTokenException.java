package ramzanlabs.imessage.user.validation.exception;

public class NoSuchEmailValidationTokenException extends Exception{

    public NoSuchEmailValidationTokenException(String token) {
        super(token);
    }
}
