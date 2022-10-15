package ramzanlabs.imessage.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ramzanlabs.imessage.discussion.DiscussionService;
import ramzanlabs.imessage.message.exception.MessageNotFoundException;
import ramzanlabs.imessage.message.exception.MessageNotInDiscussionException;
import ramzanlabs.imessage.message.exception.MessageNotSentByCurrentUserException;
import ramzanlabs.imessage.message.exception.UserNotInDiscussionException;
import ramzanlabs.imessage.message.payload.MessageSetGetRequest;
import ramzanlabs.imessage.message.payload.MessageSetPostRequest;
import ramzanlabs.imessage.user.User;
import ramzanlabs.imessage.user.UserService;
import ramzanlabs.imessage.user.auth.UserAuth;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;
    private final DiscussionService discussionService;

    public MessageController(MessageService messageService,
                             UserService userService,
                             DiscussionService discussionService) {
        this.messageService = messageService;
        this.userService = userService;
        this.discussionService = discussionService;
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
                .limitMessages(limit);
        List<Message> messages = messageService.getMessageList(messageSetRequest);
        if (messages != null) {
            return ResponseEntity.ok(
                    messages.stream().map(
                            Message::toPayload
                    )
            );
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // TODO: WORK on this later
    @GetMapping("/discussion/{discussionId}/messages/more/before/{messageId}")
    public ResponseEntity<?> getMoreMessagesInDiscussion(@PathVariable("discussionId") Long discussionId, @PathVariable("messageId") Long messageId) {
        try {
            return ResponseEntity.ok(
                    messageService.getLastDiscussionMessagesBeforeMessage(discussionId, messageId));
        } catch (MessageNotFoundException exception) {
            return ResponseEntity.notFound().build();
        } catch (Throwable e) {
            return ResponseEntity.internalServerError().build();
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
    public ResponseEntity<?> deleteMessage(@PathVariable("messageId") Long messageId,
                                           @RequestParam("discussionID") Long discussionId,
                                           Principal principal) {
        User currentUser = ((UserAuth) principal).getAuthUser();
        try {
            Long deletedMessageId = messageService.deleteMessage(currentUser, discussionId, messageId);
            return ResponseEntity.ok(deletedMessageId);
        } catch (MessageNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UserNotInDiscussionException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        } catch (MessageNotSentByCurrentUserException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        } catch (MessageNotInDiscussionException e) {
            return ResponseEntity.notFound().build();
        } catch (Throwable e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @PatchMapping("/message/{messageId}/update")
    public ResponseEntity<?> updateMessage(@PathVariable("messageId") Long messageId,
                                           @RequestParam Long discussionId,
                                           @RequestBody String content, Principal auth) {
        User currentUser = ((UserAuth) auth).getAuthUser();
        try {
            Message updatedMessage = messageService.updateMessage(currentUser, discussionId, messageId, content);
            return ResponseEntity.ok(updatedMessage);
        } catch (MessageNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (MessageNotSentByCurrentUserException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        } catch (MessageNotInDiscussionException e) {
            return ResponseEntity.notFound().build();
        } catch (Throwable e) {
            return ResponseEntity.internalServerError().build();
        }/*
        Message updatedMessage = messageService.updateMessage(messageId, content);
        if (Objects.nonNull(updatedMessage)) {
            return ResponseEntity.ok(updatedMessage);
        } else {
            return ResponseEntity.notFound().build();
        }*/
    }
}
