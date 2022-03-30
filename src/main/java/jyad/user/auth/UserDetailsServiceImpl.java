package jyad.user.auth;

import jyad.user.User;
import jyad.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUsersByUserNameOrEmail(username, username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        UserDetails userDetails =
                org.springframework.security.core.userdetails.User.withUsername(user.getUserName())
                        .password(user.getPassword())
                        .roles(Roles.ROLE_USER)
                        .build();
        return userDetails;

    }

    public static final class Roles {
        public static final String ROLE_USER = "USER";
    }
}
