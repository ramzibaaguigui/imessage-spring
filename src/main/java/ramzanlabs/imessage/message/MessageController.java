package ramzanlabs.imessage.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ramzanlabs.imessage.date.TimeUtils;
import ramzanlabs.imessage.discussion.DiscussionService;
import ramzanlabs.imessage.message.payload.MessageSetGetRequest;
import ramzanlabs.imessage.message.payload.MessageSetPostRequest;
import ramzanlabs.imessage.user.UserService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class MessageController {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    DiscussionService discussionService;

    @Autowired
    private TimeUtils time;

    @GetMapping("/message/{messageId}")
    public ResponseEntity<Message> getMessage(@PathVariable Long messageId) {
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            return ResponseEntity.ok(message);
        }
        return ResponseEntity.badRequest().build();
    }

    // TODO: CONSIDER REFACTORING THIS
    @GetMapping("/discussion/{discussionId}/messages/initial")
    public ResponseEntity<?>
    getInitialMessagesInDiscussion(@PathVariable("discussionId") Long discussionId,
                                   @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        var messageSetRequest = MessageSetGetRequest.create()
                .discussionId(discussionId)
                .limitMessages(limit)
                .sentBefore(time.now());
        List<Message> messages = messageService.getMessageList(messageSetRequest);
        if (messages != null) {
            System.out.println("printing the message list");
            System.out.println(messages.stream().map(Message::toPayload).collect(Collectors.toList()));
            return ResponseEntity.ok(
                    messages.stream().map(
                            Message::toPayload
                    ).collect(Collectors.toList())
            );
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // TODO: WORK on this later
    @GetMapping("/discussion/{discussionId}/messages/more")
    public ResponseEntity<?> getMoreMessagesInDiscussion() {
        return null;
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
