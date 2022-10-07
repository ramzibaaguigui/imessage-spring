package ramzanlabs.imessage.user.validation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailValidationRepository extends JpaRepository<EmailValidation, Long> {

}
