package ramzanlabs.imessage.websocket;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.socket.config.annotation.*;
import ramzanlabs.imessage.headers.Headers;
import ramzanlabs.imessage.user.auth.UserAuthService;
import ramzanlabs.imessage.user.auth.UserAuthPool;

import static ramzanlabs.imessage.headers.Headers.USER_AUTH_TOKEN;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer, WebSocketConfigurer {

    private final Logger logger = Logger.getLogger(WebSocketConfig.class);

    private final UserAuthService userAuthService;
    private final UserAuthPool userAuthPool;
    private final WebSocketAuthenticationPool webSocketAuthenticationPool;

    @Autowired
    public WebSocketConfig(UserAuthService userAuthService,
                           WebSocketAuthenticationPool webSocketAuthenticationPool,
                           UserAuthPool userAuthPool) {
        this.userAuthService = userAuthService;
        this.webSocketAuthenticationPool = webSocketAuthenticationPool;
        this.userAuthPool = userAuthPool;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").setHandshakeHandler(userAuthHandshakeHandler()).withSockJS();
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*")
                .addInterceptors(webSocketHandshakeInterceptor());
        // .setHandshakeHandler(userAuthHandshakeHandler());

    }


    @Override
    public void configureMessageBroker(org.springframework.messaging.simp.config.MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); // this is for subscribing
        registry.setApplicationDestinationPrefixes("/app"); // this is for sending
    }

    @Bean
    public UserAuthHandshakeHandler userAuthHandshakeHandler() {
        return new UserAuthHandshakeHandler();
    }

    @Bean
    public WebSocketHandshakeInterceptor webSocketHandshakeInterceptor() {
        return new WebSocketHandshakeInterceptor();
    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {

        registration.interceptors(
                commandFilterChannelInterceptor(),
                userAuthChannelInterceptor()
        );
    }

    private ChannelInterceptor userAuthChannelInterceptor() {
        return new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                String simpSessionId = extractSimpSessionId(message);
                if (messageCommandIsConnect(message)) {
                    // we should verify the auth
                    String authToken = extractMessageAuthToken(message);
                    if (authToken == null) {
                        System.out.println("the auth token is null");
                        return null;

                    }

                    Authentication auth = userAuthPool.validateAuthentication(authToken);
                    if (auth == null) { // auth not in pool
                        System.out.println("auth not in pool");
                        auth = userAuthService.validateAuthentication(authToken);
                        if (auth == null) { // auth not valid
                            System.out.println("auth not valid");
                            return null;
                        }
                        System.out.println("storing auth in websocket auth pool");
                        webSocketAuthenticationPool.storeAuthentication(simpSessionId, auth);
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                    System.out.println("returning " + message);
                    return message;
                }

                if (messageCommandIsSend(message)) {
                    Authentication auth = webSocketAuthenticationPool.validateAuthentication(simpSessionId);
                    if (auth == null) {
                        System.out.println("the auth is null");
                        return null;
                    }
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    return message;
                }
                return null;
            }
        };
    }

    private ChannelInterceptor commandFilterChannelInterceptor() {
        return new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                System.out.println("presend from command filter channel interceptor");
                if (messageCommandIsConnect(message) || messageCommandIsSend(message)) {
                    return message;
                }
                return null;
            }
        };
    }

    private ChannelInterceptor sendCommandChannelInterceptor() {
        return new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                System.out.println("presend from send command channel interceptor");
                return message;
            }
        };
    }

    private boolean messageCommandIsConnect(Message<?> message) {
        StompHeaderAccessor accessor = (StompHeaderAccessor) StompHeaderAccessor.getAccessor(message);
/*
        System.out.println("the command is: " + accessor.getCommand());

        System.out.println(StompCommand.CONNECT.equals(accessor.getCommand()));
*/
        System.out.println("from command is connect");
        System.out.println(accessor.getCommand());
        return StompCommand.CONNECT.equals(accessor.getCommand());
    }

    private boolean messageCommandIsSend(Message<?> message) {
        StompHeaderAccessor accessor = (StompHeaderAccessor) StompHeaderAccessor.getAccessor(message);

        System.out.println("from command is send");
        System.out.println(accessor.getCommand());
        return StompCommand.SEND.equals(accessor.getCommand());
    }

    @Nullable
    private String extractMessageAuthToken(Message<?> message) {
        // in order to be invoked with not error, we should ensure that the stomp command is connect

        GenericMessage<?> genericMessage = (GenericMessage<?>) message;
        return ((LinkedMultiValueMap<String, String>) genericMessage.getHeaders().get(Headers.WEBSOCKET_NATIVE_HEADERS)).get(USER_AUTH_TOKEN).get(0);
    }

    private String extractSimpSessionId(Message<?> message) {
        return (String) message.getHeaders().get(Headers.WEBSOCKET_SIMP_SESSION_ID);
    }


}
