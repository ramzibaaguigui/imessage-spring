package jyad.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.id = ?1")
    Optional<User> getUserById(Long id);

    Optional<User> getFirstByEmail(String email);

    @Query("select u from User u where u.firstName like concat('%', ?1, '%') or u.lastName like concat('%', ?2, '%')")
    User findUserByFirstNameContainsOrLastNameContains(String query, String query1);

    @Transactional
    @Modifying
    @Query("delete from User u where u.id = ?1")
    void deleteUserById(Long id);


    @Query("select u from User u where u.id in ?1")
    List<User> getUsersByIdIn(List<Long> id);
/*
    Boolean existsByEmail(String email);
    Boolean existsByUserName(String userName);*/

    //    @Query("select (count(u) > 0) from User u where u.email = ?1 or u.userName = ?2")
    @Query("select (count(u) > 0) from User u where u.email = ?1 or u.userName = ?2")
    Boolean existsByEmailOrUserName(String email, String username);

    @Query("select u from User u where u.userName = ?1")
    Optional<User> getUserByUserName(String username);

    @Query("select u from User u where u.userName in ?1")
    List<User> getUsersByUserNameIn(Set<String> usernames);

}
