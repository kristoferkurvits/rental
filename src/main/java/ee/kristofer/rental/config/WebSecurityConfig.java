package ee.kristofer.rental.config;

import ee.kristofer.rental.handler.AuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private final AuthFilter authFilter;

    public WebSecurityConfig(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
            .csrf()
            .disable();

        http
            .addFilterAfter(authFilter, BasicAuthenticationFilter.class);

        return http.build();
    }

}
