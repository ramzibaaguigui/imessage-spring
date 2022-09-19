package ramzanlabs.imessage.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ramzanlabs.imessage.date.TimeUtils;
import ramzanlabs.imessage.discussion.DiscussionService;
import ramzanlabs.imessage.message.payload.MessageSetGetRequest;
import ramzanlabs.imessage.message.payload.MessageSetPostRequest;
import ramzanlabs.imessage.message.payload.PostMessagePayload;
import ramzanlabs.imessage.user.User;
import ramzanlabs.imessage.user.UserService;
import ramzanlabs.imessage.user.auth.UserAuth;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;
    private final DiscussionService discussionService;
    private final TimeUtils time;

    @Autowired
    public MessageController(MessageService messageService,
                             UserService userService,
                             DiscussionService discussionService,
                             TimeUtils time) {
        this.messageService = messageService;
        this.userService = userService;
        this.discussionService = discussionService;
        this.time = time;
    }


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

    @GetMapping("/discussion/{discussionId}/messages/all")
    public ResponseEntity<?> getDiscussionMessages(Principal principal, @PathVariable("discussionId") Long discussionId) {

        User current = ((UserAuth) principal).getAuthUser();
        try {
            return ResponseEntity.ok(
                    messageService.getAllDiscussionMessages(current, discussionId)
            );
        } catch (AssertionError e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/discussion/{discussionId}/more")
    public ResponseEntity<?> getMoreMessages(Principal principal, @PathVariable("discussionId") Long discussionId,
                                             @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                                             @RequestParam(value = "lastId") Long lastMessageId) {
        User current = ((UserAuth) principal).getAuthUser();
        try {
            return ResponseEntity.ok(
                    messageService.getMoreMessages  (current, discussionId, lastMessageId, limit)
            );
        } catch (Exception exception) {
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

    @PostMapping("/messages/post/one")
    public ResponseEntity<?> postMessage(@RequestBody PostMessagePayload messagePayload, Principal principal) {
        User current = ((UserAuth) principal).getAuthUser();
        Message sentMessage = messageService.postMessage(current, messagePayload);
        return ResponseEntity.ok(sentMessage);
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
