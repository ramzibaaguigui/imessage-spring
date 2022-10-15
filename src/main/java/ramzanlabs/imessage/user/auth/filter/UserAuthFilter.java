package ramzanlabs.imessage.user.auth.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ramzanlabs.imessage.user.auth.UserAuthService;
import ramzanlabs.imessage.user.auth.UserAuthPool;
import ramzanlabs.imessage.user.auth.utility.AuthHeaderManipulator;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class UserAuthFilter extends OncePerRequestFilter {

    private final UserAuthService userAuthService;
    private final AuthHeaderManipulator authHeaderManipulator;
    private final UserAuthPool userAuthenticationPool;

    @Autowired
    public UserAuthFilter(UserAuthService userAuthService,
                          AuthHeaderManipulator authHeaderManipulator,
                          UserAuthPool userAuthenticationPool) {
        this.userAuthService = userAuthService;
        this.authHeaderManipulator = authHeaderManipulator;
        this.userAuthenticationPool = userAuthenticationPool;
    }

    private Logger logger = LoggerFactory.getLogger(UserAuthFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeaderManipulator.extractAuthToken(request);
        Authentication auth = userAuthenticationPool.validateAuthentication(token);
        if (auth == null) {
            auth = userAuthService.validateAuthentication(token);
        }

        if (auth == null) {
            unauthorizeResponse(response);
            logger.info("authentication invalid");
            return;

        }
        logger.info("authentication valid");
        userAuthenticationPool.storeAuthentication(auth);
        setUserAuth(auth);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return requestsAuthentication(request)
                || requestsRegistration(request)
                || connectsToWebsocket(request);
    }
    // this is to be removed later
    private boolean connectsToWebsocket(HttpServletRequest request) {
        System.out.println(request.getServletPath());
        boolean value = request.getServletPath().equals("/ws");
        System.out.println("requests message mapping" + value);
        return value;
    }

    private boolean requestsAuthentication(HttpServletRequest request) {
        System.out.println("path is /authenticate" + request.getServletPath().equals("/authenticate"));
        return request.getServletPath().equals("/authenticate");
    }

    private boolean requestsRegistration(HttpServletRequest request) {
        System.out.println("path is /user/create" + request.getServletPath().equals("/user/create"));
        return request.getServletPath().equals("/user/create");
    }

    private void unauthorizeResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("UNAUTHORIZED");
    }



    private void setUserAuth(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
