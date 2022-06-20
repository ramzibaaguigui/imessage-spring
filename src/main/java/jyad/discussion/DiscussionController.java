package jyad.discussion;

import jyad.discussion.payload.CreateDiscussionRequestPayload;
import jyad.headers.Headers;
import jyad.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

        return ResponseEntity.ok(createdDiscussion);
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
        Set<User> discussionUsers = discussionService.getDiscussionUsers(discussionId);
        if (Objects.nonNull(discussionUsers)) {
            return ResponseEntity.ok(discussionUsers);
        }

        return ResponseEntity.notFound().build();
    }


}
