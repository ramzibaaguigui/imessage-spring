package ramzanlabs.imessage.user.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {


    Optional<UserAuth> findFirstByAuthTokenEqualsAndIssuedAtEquals(String token, Instant issuedAt);

    Optional<UserAuth> findFirstByAuthTokenEquals(String token);

    @Transactional
    @Modifying
    @Query("delete from UserAuth u where u.authIsValid = false or u.expireAt < ?1")
    void deleteUserAuthsByAuthIsValidFalseOrExpireAtBefore(Instant current);
}
