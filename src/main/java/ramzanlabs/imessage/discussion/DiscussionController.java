package ramzanlabs.imessage.discussion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ramzanlabs.imessage.discussion.payload.CreateDiscussionRequestPayload;
import ramzanlabs.imessage.headers.Headers;
import ramzanlabs.imessage.user.User;

import java.util.Objects;
import java.util.Set;

@Controller
public class DiscussionController {

    @Autowired
    DiscussionService discussionService;

    @PostMapping("/discussion/create")
    public ResponseEntity<?> createDiscussion(@RequestBody CreateDiscussionRequestPayload createDiscussionRequestPayload,
                                              @RequestHeader(Headers.USER_AUTH_TOKEN) String authToken) {

        Discussion createdDiscussion = discussionService.createDiscussion(authToken, createDiscussionRequestPayload);
        if (createdDiscussion == null) {
            return ResponseEntity.badRequest().build();
        }

        System.out.println("create discussion");
        System.out.println(createdDiscussion.getUsers());
        return ResponseEntity.ok(createdDiscussion);
    }


    @GetMapping("/discussion/{id}")
    public ResponseEntity<?> getDiscussion(@PathVariable Long id) {
        return ResponseEntity.ok(discussionService.getDiscussionById(id));
    }

    @DeleteMapping("/discussion/delete/{id}")
    public ResponseEntity<?> deleteDiscussion(@PathVariable Long id,
                                              @RequestHeader(Headers.USER_AUTH_TOKEN) String authToken) {
        boolean deleted = discussionService.deleteDiscussion(authToken, id);
        return deleted ?
                ResponseEntity.ok("DISCUSSION_DELETED") : ResponseEntity.badRequest().build();

    }

    @GetMapping("/discussion/{discussionId}/users")
    public ResponseEntity<?> getDiscussionUsers(@PathVariable Long discussionId) {
        Set<User> discussionUsers = discussionService.getDiscussionUsers(discussionId);
        System.out.println("discussions: " + discussionUsers);

        if (Objects.nonNull(discussionUsers)) {
            return ResponseEntity.ok(discussionUsers);
        }

        return ResponseEntity.notFound().build();
    }


}
