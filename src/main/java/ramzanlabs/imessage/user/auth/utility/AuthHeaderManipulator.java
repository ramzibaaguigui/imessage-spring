package ramzanlabs.imessage.user.auth.utility;

import ramzanlabs.imessage.user.auth.UserAuth;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@Component
public class AuthHeaderManipulator {

    // TODO: THERE IS AN ERROR HERE,
    // TODO: YOU CANNOT WRITE THE TOKEN IN THE HTTP HEADER,
    // TODO: FIGURE OUT THE ERROR AND TRY TO SOLVE IT
    private static final String AUTH_TOKEN_HEADER_KEY = "USER_AUTH_TOKEN";
    private static final String AUTH_TOKEN_ISSUED_AT_HEADER_KEY = "USER_AUTH_TOKEN_ISSUED_AT";

    public void addAuthTokenHeader(ResponseEntity<?> response, UserAuth auth) {
//        HttpHeaders headers = response.getHeaders();
        HttpHeaders headers = HttpHeaders.writableHttpHeaders(response.getHeaders());

        headers.set(AUTH_TOKEN_HEADER_KEY, auth.getAuthToken());
        headers.set(AUTH_TOKEN_ISSUED_AT_HEADER_KEY, auth.getIssuedAt().toString());
        System.out.println("auth token header " + response.getHeaders().get(AUTH_TOKEN_HEADER_KEY));
        System.out.println("auth token issued token header " + response.getHeaders().get(AUTH_TOKEN_ISSUED_AT_HEADER_KEY));
    }



    public String extractAuthToken(HttpServletRequest request) {
        return request.getHeader(AUTH_TOKEN_HEADER_KEY);
    }

    public Instant extractAuthTokedExpiresAt(HttpServletRequest request) {
        return Instant.parse(request.getHeader(AUTH_TOKEN_ISSUED_AT_HEADER_KEY));
    }


}
