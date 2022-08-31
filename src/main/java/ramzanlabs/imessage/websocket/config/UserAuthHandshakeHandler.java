package ramzanlabs.imessage.websocket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import ramzanlabs.imessage.headers.Headers;
import ramzanlabs.imessage.user.auth.UserAuthService;

import javax.servlet.ServletContext;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UserAuthHandshakeHandler extends DefaultHandshakeHandler {

    @Autowired
    private UserAuthService userAuthService;

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        // todo: consider finding a way to bridge the sec-websocket-key to the auth header provided by the user

        System.out.println("determine user is called");
        System.out.println(attributes);
        System.out.println(request.getHeaders());
        return new Principal() {
            @Override
            public String getName() {
                return "ramziiii";
            }
        };
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
