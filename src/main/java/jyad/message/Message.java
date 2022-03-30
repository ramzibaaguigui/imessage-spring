package jyad.message;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jyad.user.User;
import jyad.discussion.Discussion;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity()
@Table(name = "messages")
public class Message {

    @Id
    @Column(name = "message_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "message_content")
    private String content;

    @Column(name = "sent_at")
    private Date sentAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "sender_user_id")
    @JsonIgnore
    private User sender;

    @ManyToOne()
    @JoinColumn(name = "discussion_id")
    @JsonIgnore
    private Discussion discussion;



}
