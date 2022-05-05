package jyad.message;

import jyad.discussion.DiscussionService;
import jyad.message.payload.MessageSetGetRequest;
import jyad.message.payload.MessageSetPostRequest;
import jyad.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
public class MessageController {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    DiscussionService discussionService;

    @GetMapping("/message/{messageId}")
    public ResponseEntity<Message> getMessage(@PathVariable Long messageId) {
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            return ResponseEntity.ok(message);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/messages/discussion/{discussionId}")
    public ResponseEntity<?>
    getMessagesInDiscussion(@RequestBody MessageSetGetRequest messageSetRequest) {
        List<Message> messages = messageService.getMessageList(messageSetRequest);
        if (messages != null) {
            return ResponseEntity.ok(messages);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/messages/post")
    public ResponseEntity<?> postMessages(@RequestBody MessageSetPostRequest messageSetPostRequest) {
        List<Message> postedMessages = messageService.postMessages(messageSetPostRequest);
        if (postedMessages != null) {
            return ResponseEntity.ok(postedMessages);
        }
        return ResponseEntity.badRequest().build();
    }


    @DeleteMapping("/message/{messageId}/delete")
    public ResponseEntity<?> deleteMessage(@PathVariable("messageId") Long messageId) {
        if (messageService.deleteMessage(messageId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PatchMapping("/message/{messageId}/update")
    public ResponseEntity<?> updateMessage(@PathVariable("messageId") Long messageId,
                                           @RequestBody String content) {
        Message updatedMessage = messageService.updateMessage(messageId, content);
        if (Objects.nonNull(updatedMessage)) {
            return ResponseEntity.ok(updatedMessage);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
