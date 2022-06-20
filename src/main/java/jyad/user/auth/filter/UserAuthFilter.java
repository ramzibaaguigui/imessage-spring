package jyad.user.auth.filter;

import jyad.user.User;
import jyad.user.auth.UserAuthService;
import jyad.user.auth.utility.AuthHeaderManipulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class UserAuthFilter extends OncePerRequestFilter {


    @Autowired
    UserAuthService userAuthService;

    @Autowired
    AuthHeaderManipulator headerManipulator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
             return;
        }

        String token = headerManipulator.extractAuthToken(request);
        User authUser = userAuthService.validateAuthentication(token);
        System.out.println(token);

        if (authUser == null) {
            unauthorizeResponse(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return requestsAuthentication(request)
                || requestsRegistration(request);
    }

    private boolean requestsAuthentication(HttpServletRequest request) {
        System.out.println(request.getServletPath().equals("/authenticate"));
        return request.getServletPath().equals("/authenticate");
    }

    private boolean requestsRegistration(HttpServletRequest request) {
        System.out.println(request.getServletPath().equals("/user/create"));
        return request.getServletPath().equals("/user/create");
    }


    private void unauthorizeResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("UNAUTHORIZED");
    }


}
