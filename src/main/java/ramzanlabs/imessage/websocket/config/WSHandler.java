package ramzanlabs.imessage.websocket.config;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;


// consider removing this class
// first removing its usages
public class WSHandler implements WebSocketHandler {
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("after connection established is called");
        System.out.println("the session headers are:");
        System.out.println(session.getHandshakeHeaders());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

        System.out.println("after connection closed is called");
        System.out.println("the session headers are:");
        System.out.println(session.getHandshakeHeaders());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
