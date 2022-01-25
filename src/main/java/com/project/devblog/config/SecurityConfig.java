package com.project.devblog.config;

import com.project.devblog.model.enums.Role;
import com.project.devblog.security.jwt.JwtConfigurer;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String USER_ENDPOINT = "/v1/user/**";
    private static final String LOGIN_ENDPOINT = "/v1/auth/**";
    private static final String REGISTRATION_ENDPOINT = "/v1/registration";
    private static final String VERIFY_ENDPOINT = "/v1/users/**/verify";
    private static final String CHECK_TOKEN_ENDPOINT = "/v1/checkToken/**";
    private static final String TOPIC_ENDPOINT = "/v1/topic/**";
    private static final String SEARCHES_ENDPOINT = "/v1/searches/**";

    private final JwtConfigurer jwtConfigurer;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(LOGIN_ENDPOINT).permitAll()
                .antMatchers(REGISTRATION_ENDPOINT).permitAll()
                .antMatchers(VERIFY_ENDPOINT).permitAll()
                .antMatchers(CHECK_TOKEN_ENDPOINT).permitAll()
                .antMatchers(USER_ENDPOINT).hasAnyRole(Role.USER.name())
                .antMatchers(TOPIC_ENDPOINT).hasAnyRole(Role.USER.name())
                .antMatchers(SEARCHES_ENDPOINT).hasAnyRole(Role.USER.name())
                .anyRequest().authenticated()
                .and()
                .apply(jwtConfigurer);
    }
}
