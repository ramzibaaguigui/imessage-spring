package jyad.user.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jyad.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user_auth")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserAuth {
    // This class contains the information related to some authentication
    // It contains the user id
    /// Alongside some other important staff
    // like the expiration date of the auth
    // and a lot of staff
    // This is primarily written by me without using spring security framework
    // But that is absolutely going to be easily implemented with SpringSecurity
    // ans UserDetailsService
    // You just need to write an implementation for it
    // And yeah here we go

    @Id
    @JsonProperty("auth_id")
    @Column(name = "auth_id")
    private Long id;


    // THIS COULD BE CHANGED LATER
    @ManyToOne
    @JoinColumn(name = "auth_user_user_id")
    @JsonProperty("auth_user")

    private User authUser;

    @JsonProperty("auth_token")
    @Column(name = "auth_token")
    private String token;



}
