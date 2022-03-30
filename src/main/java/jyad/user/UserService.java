package jyad.user;

import jyad.discussion.Discussion;
import jyad.discussion.DiscussionRepository;
import jyad.user.utils.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    DiscussionRepository discussionRepository;

    @Autowired
    UserValidator userValidator;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;


    public User updateUserFirstName(Long userId, String firstName) {

        Optional<User> user = userRepository.getUserById(userId);
        if (user.isPresent()) {
            user.get().setFirstName(firstName);
            return userRepository.save(user.get());
        }
        return null;
    }
    public User updateUserLastName(Long userId, String lastName) {

        Optional<User> user = userRepository.getUserById(userId);
        if (user.isPresent()) {
            user.get().setLastName(lastName);

            return userRepository.save(user.get());
        }

        return null;
    }

    public boolean addUserToDiscussion(Long userId, Long discussionId) {
        Optional<Discussion> discussion = discussionRepository.getDiscussionById(discussionId);
        Optional<User> user = userRepository.getUserById(userId);
        if (discussion.isPresent() && user.isPresent()) {
            discussion.get().addUser(user.get());
            discussionRepository.save(discussion.get());
            return true;
        }

        return false;
    }

    public User getUserById(Long userId) {
        Optional<User> user = userRepository.getUserById(userId);
        return user.orElse(null);
    }

    public boolean deleteUserById(Long id) {
        Optional<User> user = userRepository.getUserById(id);
        if (user.isPresent()) {
            userRepository.deleteUserById(id);
            return true;
        }

        return false;
    }

    public List<User> getDiscussionUsers(Long discussionId) {
        Optional<Discussion> discussion = discussionRepository.getDiscussionById(discussionId);
        return discussion.map(value -> (List<User>) value.getUsers()).orElse(null);

    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.getFirstByEmail(email);
        return user.orElse(null);
    }

    public List<User> getUsersByIdIn(List<Long> userIds) {
        return userRepository.getUsersByIdIn(userIds);
    }



}
