package ramzanlabs.imessage.user.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ramzanlabs.imessage.user.User;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "email_validation")
public class EmailValidation {

    @Column(name = "validation_token")
    private String validationToken;

    @Column(name = "email")
    private String email;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "is_validated")
    private Boolean isValidated;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "validated_at")
    private Date validatedAt;
}
