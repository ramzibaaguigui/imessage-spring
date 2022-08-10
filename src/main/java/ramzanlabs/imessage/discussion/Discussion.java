package ramzanlabs.imessage.discussion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Fetch;
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_discussions",
    joinColumns = @JoinColumn(name = "discussion_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnore
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


    public void addUsers(User... users) {
        for (User user: users) {
            if (!userExists(user))
                this.users.add(user);
        }
    }

    public void addMessage(Message message) {
        if (message == null) {
            return;
        }
        messages.add(message);
    }

    private boolean userExists(User user) {
        for (User u : this.getUsers()) {
            if (u.equals(user)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasUser(User user) {
        return this.users.contains(user);
    }

    public boolean hasExactUsers(User... users) {
        return this.users.containsAll(Arrays.stream(users).toList())
                && this.users.size() == users.length;
    }

    // @JsonProperty("new_discussion")
    public boolean discussionIsNew() {
        return this.messages.size() == 0;
    }

    @JsonProperty("members_count")
    public int membersCount() {
        return users.size();
    }

}
