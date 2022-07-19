package ramzanlabs.imessage.discussion;

import ramzanlabs.imessage.date.TimeUtils;
import ramzanlabs.imessage.discussion.payload.CreateDiscussionRequestPayload;
import ramzanlabs.imessage.discussion.payload.DiscussionCreationRequest;
import ramzanlabs.imessage.message.MessageRepository;
import ramzanlabs.imessage.user.User;
import ramzanlabs.imessage.user.UserRepository;
import ramzanlabs.imessage.user.auth.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DiscussionService {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    DiscussionRepository discussionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TimeUtils time;

    @Autowired
    UserAuthService userAuthService;

    public Discussion getDiscussionById(Long id) {
        Optional<Discussion> discussion = discussionRepository.getDiscussionById(id);
        return discussion.orElse(null);
    }

    public List<Discussion> getUserDiscussions(Long userId) {
        Optional<User> user = userRepository.getUserById(userId);
        if (user.isPresent()) {
            return user.get().getDiscussions();
        } else {
            return null;
        }
    }

    public void removeUserFromDiscussion(Long discussionId, Long userId) {
        Optional<Discussion> discussion = discussionRepository.getDiscussionById(discussionId);
        Optional<User> user = userRepository.getUserById(userId);
        if (user.isPresent() && discussion.isPresent()) {
            discussion.get().getUsers().remove(user.get());
            // saving entities
            discussionRepository.save(discussion.get());
            // todo: check whether this is mandatory later
            // userRepository.save(user.get());
        }
    }

    public Discussion updateDiscussionName(Long discussionId, String newName) {
        Optional<Discussion> discussion = discussionRepository.getDiscussionById(discussionId);
        if (discussion.isPresent()) {
            discussion.get().setName(newName);
            return discussionRepository.save(discussion.get());
        }
        return null;
    }

    public void addUserToDiscussion(Long discussionId, Long userId) {
        Optional<Discussion> discussion = discussionRepository.getDiscussionById(discussionId);
        Optional<User> user = userRepository.getUserById(userId);

        if (discussion.isPresent() && user.isPresent()) {
            discussion.get().addUser(user.get());
            Discussion savedDiscussion = discussionRepository.save(discussion.get());
        }
    }

    public Discussion createDiscussion(DiscussionCreationRequest discussionCreationRequest) {
        List<User> users = userRepository.getUsersByIdIn(discussionCreationRequest.getUsersIds());
        Optional<User> createdBy = userRepository.getUserById(discussionCreationRequest.getCreatedById());

        if (createdBy.isPresent()) {

            Discussion discussion = new Discussion();
            for (User user : users) {
                discussion.addUser(user);
            }
            discussion.setName(discussionCreationRequest.getName());
            discussion.setCreatedBy(createdBy.get());
            discussion.setCreatedAt(time.now());
            return discussionRepository.save(discussion);
        } else {
            return null;
        }
    }

    public Discussion createDiscussion(String authToken, CreateDiscussionRequestPayload createDiscussionRequestPayload) {
        List<User> users = userRepository.getUsersByUserNameIn(createDiscussionRequestPayload.getDiscussionUsersUsernames());
        User createdBy = userAuthService.validateUserAuthentication(authToken);
        if (createdBy == null) {
            return null;
        }

        Discussion discussion = new Discussion();
        discussion.addUser(createdBy);
        for (User user: users) {
            discussion.addUser(user);
        }
        discussion.setName(createDiscussionRequestPayload.getDiscussionName());
        discussion.setCreatedBy(createdBy);
        discussion.setCreatedAt(time.now());
        return discussionRepository.save(discussion);
    }

    public void removeDiscussion(String discussionId) {
        discussionRepository.deleteDiscussionById(discussionId);
    }


    public boolean deleteDiscussion(String authToken, Long discussionId) {
        Optional<Discussion> discussion = discussionRepository.findById(discussionId);
        User user = userAuthService.validateUserAuthentication(authToken);
        if (discussion.isEmpty() || user == null) {
            return false;
        }

        if (discussion.get().getCreatedBy().equals(user)) {
            discussionRepository.delete(discussion.get());
            return true;
        }
        return false;
    }


    public Set<User> getDiscussionUsers(Long discussionId) {
        Optional<Discussion> discussion = discussionRepository.getDiscussionById(discussionId);
        return discussion.map(Discussion::getUsers).orElse(null);
    }
}
