package ramzanlabs.imessage;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ramzanlabs.imessage.message.MessageService;
import ramzanlabs.imessage.message.payload.MessageSetGetRequest;

@SpringBootTest
public class MessageTests {

    @Autowired
    private MessageService messageService;

    @Test
    public void testMessages() {

    }
}
