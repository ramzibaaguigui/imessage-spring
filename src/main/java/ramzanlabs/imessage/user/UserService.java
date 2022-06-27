package ramzanlabs.imessage.user;

import ramzanlabs.imessage.discussion.Discussion;
import ramzanlabs.imessage.discussion.DiscussionRepository;
import ramzanlabs.imessage.user.auth.UserAuthService;
import ramzanlabs.imessage.user.utils.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


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

    @Autowired
    UserAuthService userAuthService;


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

    public List<Discussion> getUserDiscussions(Long userId) {
        Optional<User> user = userRepository.getUserById(userId);
        return user.map(User::getDiscussions).orElse(null);
    }

    public List<Discussion> getUserDiscussions(String authToken) {
        User user = userAuthService.validateAuthentication(authToken);
        System.out.println(user);
        if (user == null) {
            return null;
        }

        return user.getDiscussions();
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

    @Nullable
    public User findUserByUsernameAndPassword(String username, String password) {
        Optional<User> authUser = userRepository.findAll()
                .stream()
                .filter(user -> (user.getUserName().equals(username) && passwordEncoder.matches(password, user.getPassword())))
                .findAny();
        if (authUser.isPresent()) {
            return authUser.get();
        }
        return null;
    }

    public Set<User> searchUsers(String searchQuery) {
        if (searchQuery == null) {
            return null;
        }

        Set<User> users = userRepository.findAll()
                .stream().filter(
                        user ->
                                (user.getFirstName().toLowerCase().contains(searchQuery)
                                        || user.getLastName().toLowerCase().contains(searchQuery.toLowerCase())
                                        || user.getUserName().toLowerCase().contains(searchQuery.toLowerCase())
                                ))
                .collect(Collectors.toSet());
        return users;
    }
}
