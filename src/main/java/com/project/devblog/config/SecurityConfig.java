package com.project.devblog.config;

import com.project.devblog.handler.Oauth2AuthenticationSuccessHandler;
import com.project.devblog.model.enums.Role;
import com.project.devblog.repository.UserRepository;
import com.project.devblog.security.CustomAccessDeniedHandler;
import com.project.devblog.security.CustomAuthenticationEntryPoint;
import com.project.devblog.security.JwtTokenAuthorizationFilter;
import com.project.devblog.security.JwtTokenProvider;
import com.project.devblog.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] AUTH_URL = {"/v1/auth/**", "/v1/oauth2/**", "/login"};
    private static final String GOOGLE_AUTH_REDIRECT_URL = "/";
    private static final String[] OPEN_API_URLS = {
            "/api-docs/**",
            "/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/actuator/**",
            "/management/**"
    };
    private final JwtTokenAuthorizationFilter jwtTokenAuthorizationFilter;
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
//                .authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers(OPEN_API_URLS).permitAll()
                .antMatchers(AUTH_URL).permitAll()
//                .antMatchers(GOOGLE_AUTH_REDIRECT_URL).permitAll()
                .antMatchers("/v1/articles/**").permitAll()
                .antMatchers("/v1/tags/**").hasAuthority(Role.ADMIN.toString())
//                .antMatchers("/v1/users").hasAuthority(Role.ADMIN.toString())
                .antMatchers("/v1/users/**").hasAnyAuthority(Role.USER.toString(), Role.ADMIN.toString())
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtTokenAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login()/*.loginPage("/login")*/
                .successHandler(new Oauth2AuthenticationSuccessHandler(userService, userRepository, jwtTokenProvider))
        /*.failureHandler(new Oauth2AuthenticationFailureHandler())*/
        ;
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

    // Used by spring security if CORS is enabled.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
