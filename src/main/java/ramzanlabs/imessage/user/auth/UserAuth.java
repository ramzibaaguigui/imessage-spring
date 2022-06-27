package ramzanlabs.imessage.user.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ramzanlabs.imessage.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "user_auth")
public class UserAuth {


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

}
