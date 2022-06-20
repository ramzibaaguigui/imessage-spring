package jyad.user.auth.payload;

import java.time.Instant;

public class UserLogoutRequestPayload {

    private String token;
    private Instant tokenIssuedAt;

    public UserLogoutRequestPayload() {}
    public UserLogoutRequestPayload(String token, Instant tokenIssuedAt) {
        this.token = token;
        this.tokenIssuedAt = tokenIssuedAt;
    }

    public String getToken() {
        return this.token;
    }

    public Instant getTokenIssuedAt() {
        return this.tokenIssuedAt;
    }


}
