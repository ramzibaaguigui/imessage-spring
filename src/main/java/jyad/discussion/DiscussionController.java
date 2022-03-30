package jyad.discussion;

import jyad.discussion.payload.DiscussionCreationRequest;
import jyad.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
public class DiscussionController {

    @Autowired
    DiscussionService discussionService;

    @PostMapping("/discussion/create")
    public ResponseEntity<?> createDiscussion(@RequestBody DiscussionCreationRequest discussionCreationRequest) {
        return ResponseEntity.ok(
                discussionService.createDiscussion(discussionCreationRequest)
        );
    }

    @GetMapping("/discussion/{id}")
    public ResponseEntity<?> getDiscussion(@PathVariable Long id) {
        return ResponseEntity.ok(discussionService.getDiscussionById(id));
    }

    @DeleteMapping("/discussion/delete/{id}")
    public void deleteDiscussion(@PathVariable String id) {
        discussionService.deleteDiscussion(id);
    }

    @GetMapping("/discussion/{discussionId}/users/")
    public ResponseEntity<?> getDiscussionUsers(@PathVariable Long discussionId) {
        List<User> discussionUsers = discussionService.getDiscussionUsers(discussionId);
        if (Objects.nonNull(discussionUsers)) {
            return ResponseEntity.ok(discussionUsers);
        }

        return ResponseEntity.notFound().build();
    }


}
