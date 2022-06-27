package ramzanlabs.imessage.user.auth.config;

import java.time.Instant;

public class TokenDuration {
    private static final long TOKEN_VALIDITY_DURATION_HOURS = 48;
    private static final long TOKEN_VALIDITY_DURATION_MINUTES = TOKEN_VALIDITY_DURATION_HOURS * 60;
    private static final long TOKEN_VALIDITY_DURATION_SECONDS = TOKEN_VALIDITY_DURATION_MINUTES * 60;
    private static final long TOKEN_VALIDITY_DURATION_MILLIS = TOKEN_VALIDITY_DURATION_SECONDS * 1000;
    public static Instant getTokenExpireAt(Instant issuedAt) {
        return issuedAt.plusMillis(TOKEN_VALIDITY_DURATION_MILLIS);
    }


}
