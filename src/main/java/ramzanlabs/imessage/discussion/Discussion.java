package ramzanlabs.imessage.discussion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ramzanlabs.imessage.message.Message;
import ramzanlabs.imessage.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "discussions")
public class Discussion {

    @Id
    @Column(name = "discussion_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    @JoinTable(name = "users_discussions",
    joinColumns = @JoinColumn(name = "discussion_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
//    @JsonIgnore
    private Set<User> users = new HashSet<>();

    @Column(name = "discussion_name")
    private String name;

    @OneToMany(mappedBy = "discussion")
    @JsonIgnore
    private List<Message> messages = new ArrayList<>();

    @ManyToOne
    @JsonProperty("created_by")
    private User createdBy;

    @Column(name = "created_at")
    @JsonProperty("created_at")
    private Date createdAt;


    public void addUser(User user) {
        if (!userExists(user))
            this.users.add(user);
    }

    private boolean userExists(User user) {
        for (User u : this.getUsers()) {
            if (u.equals(user)) {
                return true;
            }
        }
        return false;
    }

}
