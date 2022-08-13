package ramzanlabs.imessage.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ramzanlabs.imessage.user.auth.filter.UserAuthFilter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private UserAuthFilter userAuthFilter;

    @Autowired
    public void setUserAuthFilter(@Lazy UserAuthFilter userAuthFilter) {
        this.userAuthFilter = userAuthFilter;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(userAuthFilter, BasicAuthenticationFilter.class);
        http.authorizeRequests()
                .antMatchers("/authenticate")
                .permitAll()
                .and().authorizeRequests()
                .anyRequest().authenticated()
                .and().cors()
                .and().csrf().disable();
    }

}
