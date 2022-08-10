package ramzanlabs.imessage.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.config.annotation.*;
import ramzanlabs.imessage.user.auth.UserAuthService;

import static ramzanlabs.imessage.headers.Headers.USER_AUTH_TOKEN;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer, WebSocketConfigurer {

    private final UserAuthService userAuthService;
    private final WebSocketAuthenticationPool webSocketAuthenticationPool;

    @Autowired
    public WebSocketConfig(UserAuthService userAuthService,
                           WebSocketAuthenticationPool webSocketAuthenticationPool) {
        this.userAuthService = userAuthService;
        this.webSocketAuthenticationPool = webSocketAuthenticationPool;
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

        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
/*
                StompHeaderAccessor accessor = (StompHeaderAccessor) StompHeaderAccessor.getAccessor(message);
                System.out.println("the stomp command is");
                System.out.println(accessor.getCommand());
                System.out.println(accessor.toNativeHeaderMap());

                System.out.println(message.getHeaders());
                return message;*/
                String authToken = extractMessageAuthTokenHeader(message);
                if (authToken == null) {
                    System.out.println("the auth token is null");
                    return null;
                }
                Authentication auth = webSocketAuthenticationPool.validateAuthentication(authToken);
                if (auth == null) {
                    auth = userAuthService.validateAuthentication(authToken);
                }

                if (auth == null) {
                    return null;
                }
                webSocketAuthenticationPool.storeAuthentication(auth);
                return message;
            }

        });
    }

    private boolean messageCommandIsConnect(Message<?> message) {
        StompHeaderAccessor accessor = (StompHeaderAccessor) StompHeaderAccessor.getAccessor(message);
        System.out.println("the command is connect");
        System.out.println(StompCommand.CONNECT.equals(accessor.getCommand()));
        return StompCommand.CONNECT.equals(accessor.getCommand());


    }

    private String extractMessageAuthTokenHeader(Message<?> message) {
        StompHeaderAccessor accessor = (StompHeaderAccessor) StompHeaderAccessor.getAccessor(message);
        return accessor.getFirstNativeHeader(USER_AUTH_TOKEN);

    }


}
