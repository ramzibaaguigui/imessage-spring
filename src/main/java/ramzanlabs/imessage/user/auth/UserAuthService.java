package ramzanlabs.imessage.user.auth;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ramzanlabs.imessage.user.UserService;
import ramzanlabs.imessage.user.auth.config.TokenDuration;
import ramzanlabs.imessage.user.auth.payload.UserAuthRequestPayload;
import ramzanlabs.imessage.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class UserAuthService {
    private static final long EVERY_MINUTE = 60 * 1000;

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    UserService userService;

    @Autowired
    AuthTokenGenerator authTokenGenerator;

    public UserAuth authenticate(UserAuthRequestPayload authRequestPayload) {
        User user = userService.findUserByUsernameAndPassword(
                authRequestPayload.getUsername(),
                authRequestPayload.getPassword()
        );

        if (user != null) {
            return generateAuthForUser(user);

        }
        return null;
    }

    public boolean logout(String authToken, Instant issuedAt) {
        Optional<UserAuth> userAuth =
                userAuthRepository.findFirstByAuthTokenEqualsAndIssuedAtEquals(authToken, issuedAt);
        if (userAuth.isPresent()) {
            invalidateUserAuth(userAuth.get());
            return true;
        }
        return false;
    }

    private UserAuth invalidateUserAuth(UserAuth userAuth) {
        userAuth.setAuthIsValid(false);
        return userAuthRepository.save(userAuth);
    }

    //TODO: consider fixing this, it returns null even if the token is valid
    public User validateUserAuthentication(String token) {
        if (token == null){
            return null;
        }

        Optional<UserAuth> userAuth = userAuthRepository.findFirstByAuthTokenEquals(token);
        if (userAuth.isPresent()) {
            if (authIsValid(userAuth.get())) {
                return userAuth.get().getAuthUser();
            }
        }
        return null;
    }

    public Authentication validateAuthentication(String authToken) {
        System.out.println("validating authentication for " + authToken);
        if (authToken == null) {
            System.out.println("auth token is null");
            return null;

        }
        System.out.println("auth token is not null");
        System.out.println();
        Optional<UserAuth> userAuth = userAuthRepository.findFirstByAuthTokenEquals(authToken);

        if (userAuth.isPresent()) {
            System.out.println("authentication is present in database");
            if (authIsValid(userAuth.get())) {
                return userAuth.get();
            }
        }
        System.out.println("authentication not found in database");
        return null;
    }



    private UserAuth generateAuthForUser(User user) {
        Instant issuedAt = Instant.now();
        Instant expireAt = TokenDuration.getTokenExpireAt(issuedAt);
        String authToken = authTokenGenerator.generateRandomToken();

        UserAuth userAuth = UserAuth.forUser(user, authToken, issuedAt, expireAt);
        userAuth = userAuthRepository.save(userAuth);
        return userAuth;
    }


    private boolean authIsValid(@NonNull UserAuth userAuth) {
        return userAuth.getAuthIsValid()
                && userAuth.getExpireAt().isAfter(Instant.now());
    }

    @Scheduled(fixedDelay = EVERY_MINUTE)
    public void removeInvalidAuthentications() {
        userAuthRepository.deleteUserAuthsByAuthIsValidFalseOrExpireAtBefore(Instant.now());
    }



}
