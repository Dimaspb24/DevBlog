package com.project.devblog.config;

import com.project.devblog.security.CustomAccessDeniedHandler;
import com.project.devblog.security.CustomAuthenticationEntryPoint;
import com.project.devblog.security.JwtTokenAuthorizationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenAuthorizationFilter jwtTokenAuthorizationFilter;
    private static final String AUTH_URL = "/v1/auth/**";
    private static final String GOOGLE_AUTH_REDIRECT_URL = "/";
    private static final String[] DOMAIN_URLS = {
            "/v1/users/**",
            "/v1/articles/**",
            "/v1/tags/**",
    };
    private static final String[] OPEN_API_URLS = {
            "/api-docs/**",
            "/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers(OPEN_API_URLS).permitAll()
                .antMatchers(AUTH_URL).permitAll()
//                .antMatchers(DOMAIN_URLS).hasRole(Role.USER.toString())
//                .antMatchers(GOOGLE_AUTH_REDIRECT_URL).hasRole(Role.USER.toString())
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(jwtTokenAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login();
    }

    @Bean
    public CustomAccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public CustomAuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
