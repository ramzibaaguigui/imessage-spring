package ramzanlabs.imessage.user.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ramzanlabs.imessage.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "user_auth")
public class UserAuth implements Authentication {


    private UserAuth(User user, String token, Instant issuedAt, Instant expireAt) {
        this.authUser = user;
        this.authToken = token;
        this.issuedAt = issuedAt;
        this.expireAt = expireAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, updatable = false, nullable = false)
    @JsonIgnore
    private Long id;


    @ManyToOne
    @JoinColumn(name = "auth_user_user_id", updatable = false, nullable = false)
    @JsonProperty("auth_user")
    private User authUser;

    @Column(name = "auth_token", unique = true, nullable = false, updatable = false)
    @JsonProperty("auth_token")
    private String authToken;


    @Column(name = "issued_at")
    @JsonProperty("issued_at")
    private Instant issuedAt;


    @Column(name = "expires_at")
    @JsonProperty("expires_at")
    private Instant expireAt;

    @JsonIgnore
    @Column(name = "auth_is_valid", nullable = false)
    private Boolean authIsValid = true;

    static UserAuth forUser(User user, String authToken, Instant issuedAt, Instant expireAt) {
        return new UserAuth(user, authToken, issuedAt, expireAt);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role: authUser.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return new UserCredentials(authUser.getUserName(), authUser.getPassword());
    }

    @Override
    public Object getDetails() {
        return new UserDetails(authUser.getFirstName(), authUser.getLastName());
    }

    @Override
    public Object getPrincipal() {
        return authUser;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return authToken;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static class UserCredentials {
        private String username;
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static class UserDetails {
        private String firstName;
        private String lastName;
    }

}
