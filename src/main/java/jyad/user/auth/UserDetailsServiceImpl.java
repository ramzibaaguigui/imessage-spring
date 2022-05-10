package jyad.user.auth;

import jyad.user.User;
import jyad.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class qUserDetailsServiceImpl implements UserDetailsService {

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
                        .roles()
                        .build();
        return userDetails;

    }

    public static final class Roles {
        public static final String ROLE_USER = "USER";
    }
}
