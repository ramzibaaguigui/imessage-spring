package ramzanlabs.imessage.user.auth;

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
    UserAuthRepository userAuthRepository;

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
    public User validateAuthentication(String token) {
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