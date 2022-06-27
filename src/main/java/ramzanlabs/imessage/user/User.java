package ramzanlabs.imessage.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ramzanlabs.imessage.discussion.Discussion;
import ramzanlabs.imessage.user.auth.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Column(name = "user_id", unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    @Column(name = "last_name")
    @JsonProperty("last_name")
    private String lastName;

    @Column(name = "phone_number", unique = true)
    @JsonProperty(value = "phone_number", access = JsonProperty.Access.WRITE_ONLY)
    private String phoneNumber;

    @Column(name = "image_url")
    @JsonProperty("image_url")
    private String imageUrl;

    @Column(name = "email", unique = true)
    @JsonProperty(value = "email", access = JsonProperty.Access.WRITE_ONLY)
    private String email;

    @Column(name = "user_name", unique = true)
    @JsonProperty("user_name")
    private String userName;

    @Column(name = "password")
    @JsonProperty(
            value = "password",
            access = JsonProperty.Access.WRITE_ONLY)
    private String password;


    @ManyToMany(mappedBy = "users", targetEntity = Discussion.class)
    @JsonIgnore
    private List<Discussion> discussions;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

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

    @JsonIgnore
    public Set<String> getRoleNames() {
        return this.roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(userName, user.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName);
    }

    public void addDiscussion(Discussion discussion) {
        this.discussions.add(discussion);
    }
}
