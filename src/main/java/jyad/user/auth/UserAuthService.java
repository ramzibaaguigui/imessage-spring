package jyad.user.auth;

import jyad.user.User;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService {
    @Autowired
    UserAuthRepository authRepository;
    @Autowired
    AuthTokenGenerator authTokenGenerator;


    public String generateAuthToken(User user) {
        UserAuth auth = new UserAuth();
        auth.setAuthUser(user);
        auth.setToken(authTokenGenerator.generateRandomAuthToken());

        auth = authRepository.save(auth);
        return auth.getToken();
    }
}
