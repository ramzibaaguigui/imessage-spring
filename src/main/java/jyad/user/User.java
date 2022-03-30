package jyad.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jyad.discussion.Discussion;
import jyad.user.auth.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
@Indexed
public class User {


    @Column(name = "first_name")
    @JsonProperty("first_name")
    private String firstName;


    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("user_id")
    private Long id;

    @Column(name = "last_name")
    @JsonProperty("last_name")
    private String lastName;

    @Column(name = "phone_number")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @Column(name = "image_url")
    @JsonProperty("image_url")
    private String imageUrl;

    @Column(name = "email")
    @JsonProperty("email")
    private String email;

    @Column(name = "user_name")
    @JsonProperty("user_name")
    private String userName;

    @Column(name = "password")
    @JsonProperty(
            namespace = "password",
            access = JsonProperty.Access.WRITE_ONLY)
    private String password;


    @ManyToMany
    @JoinTable(
            name = "users_discussions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "discussion_id")
    )
    @JsonIgnore
    private List<Discussion> discussions;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Roles.Role> roles;

    public boolean equals(User user) {
        return this.getId().equals(user.getId());
    }

    public void trim() {
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.userName = userName.trim();
        this.email = email.trim();
        this.phoneNumber = phoneNumber.trim();
    }

    public boolean passwordMeetsRequirements() {
        try {
            return this.password.length() >= 8;
        } catch (Exception exception) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", id=" + id +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", discussions=" + discussions +
                ", roles=" + roles +
                '}';
    }
}
