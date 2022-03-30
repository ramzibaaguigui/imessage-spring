package jyad.discussion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jyad.message.Message;
import jyad.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "discussions")
public class Discussion {

    @Id
    @Column(name = "discussion_id")
    private Long id;

    @ManyToMany(mappedBy = "discussions")
    @JsonIgnore
    private List<User> users;

    @Column(name = "discussion_name")
    private String name;

    @OneToMany(mappedBy = "discussion")
    @JsonIgnore
    private Set<Message> messages;

    @ManyToOne
    private User createdBy;

    @Column(name = "created_at")
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
