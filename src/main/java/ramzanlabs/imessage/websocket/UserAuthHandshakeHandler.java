package ramzanlabs.imessage.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import ramzanlabs.imessage.headers.Headers;
import ramzanlabs.imessage.user.auth.UserAuthService;

import javax.servlet.ServletContext;
import java.security.Principal;
import java.util.List;
import java.util.Map;

public class UserAuthHandshakeHandler extends DefaultHandshakeHandler {

    @Autowired
    private UserAuthService userAuthService;

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {

        System.out.println(attributes);
        System.out.println("determine user is called");
        String authToken = extractAuthToken(request);
        System.out.println("the extracted auth token header is");
        System.out.println(authToken);
        System.out.println(request.getPrincipal());
        Authentication auth = userAuthService.validateAuthentication(authToken);
        if (auth == null) {
            return null;
        }
        ;

        return auth;
        // todo: consider adding the auth with the first frame received from the user
        // the websocket is going to be open for everyone to connect
        // but right after the connection is made
        // there should be a sent frame containing the auth information
        // if the user does not send them in the necessary time, consider finding a way to close the connection

    }


    private String extractAuthToken(ServerHttpRequest request) {

        List<String> authTokenHeader = request.getHeaders().get(Headers.USER_AUTH_TOKEN);
        System.out.println(request.getHeaders());
        System.out.println("auth headers are:");
        System.out.println(authTokenHeader);
        if (authTokenHeader == null) {
            return null;
        }

        if (authTokenHeader.size() == 1) {
            return authTokenHeader.get(0);
        }
        return null;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        super.setServletContext(servletContext);

    }

    @Override
    protected boolean isWebSocketVersionSupported(WebSocketHttpHeaders httpHeaders) {
        System.out.println("these are the http headers");
        System.out.println(httpHeaders.keySet());
        return true;
    }
}
