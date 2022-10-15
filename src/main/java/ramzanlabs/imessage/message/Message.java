package ramzanlabs.imessage.message;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ramzanlabs.imessage.discussion.Discussion;
import ramzanlabs.imessage.message.payload.MessagePayload;
import ramzanlabs.imessage.user.User;
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
    @JsonProperty("message_id")
    private Long id;

    @Column(name = "message_content")
    @JsonProperty("message_content")
    private String content;

    @Column(name = "sent_at")
    @JsonProperty("sent_at")
    private Date sentAt;

    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    private Date updatedAt;

    @Column(name = "is_deleted")
    @JsonIgnore
    private Boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "sender_user_id")
    @JsonIgnore
    private User sender;

    @ManyToOne()
    @JoinColumn(name = "discussion_id")
    @JsonIgnore
    private Discussion discussion;

    @JsonProperty("sender_id")
    public Long getSenderId() {
        return this.sender.getId();
    }

    @JsonProperty("discussion_id")
    public Long getDiscussionId() {
        return this.discussion.getId();
    }

    public MessagePayload toPayload() {
        return MessagePayload.create()
                .withId(getId())
                .withContent(getContent())
                .sentAt(getSentAt())
                .updatedAt(getUpdatedAt())
                .senderUsername(getSender().getUserName());
    }


    public boolean isInDiscussion(Discussion discussion) {
        return this.getDiscussion().getId().equals(discussion.getId());
    }

    public boolean isSentBy(User user) {
        return this.getSender().getId().equals(user.getId());
    }
}
