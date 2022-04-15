package com.project.devblog.config;

import com.project.devblog.handler.CustomAccessDeniedHandler;
import com.project.devblog.handler.CustomAuthenticationEntryPoint;
import com.project.devblog.model.enums.Role;
import com.project.devblog.security.CustomUserDetailsService;
import com.project.devblog.security.JwtTokenFilter;
import com.project.devblog.security.oauth2.CustomOAuth2UserService;
import com.project.devblog.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.project.devblog.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.project.devblog.security.oauth2.OAuth2AuthenticationSuccessHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final JwtTokenFilter jwtTokenFilter;
    private final PasswordEncoder passwordEncoder;

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    /*
      By default, Spring OAuth2 uses HttpSessionOAuth2AuthorizationRequestRepository to save
      the authorization request. But, since our service is stateless, we can't save it in
      the session. We'll save the request in a Base64 encoded cookie instead.
    */
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    //    @Bean(BeanIds.AUTHENTICATION_MANAGER) // ???
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint()) // при включении не получается зайти на /login
                .and()
                .authorizeRequests()
                .antMatchers(OPEN_API_URLS).permitAll()
                .antMatchers(AUTH_URL).permitAll()
                .antMatchers(GOOGLE_AUTH_REDIRECT_URL).permitAll()
                .antMatchers("/v1/articles/**").permitAll()
                .antMatchers("/v1/tags/**").hasAuthority(Role.ADMIN.toString())
                .antMatchers("/v1/users/**").hasAnyAuthority(Role.USER.toString(), Role.ADMIN.toString()) // при включении возвращается 403 при попытке /v1/users/id
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/login")
                .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
                .and()
                .redirectionEndpoint()
                .baseUri("/v1/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);
        /*.loginPage("/login")*/
        /*.loginPage( "/login/oauth2" )*/
        ;

        // Add our custom Token based authentication filter
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CustomAccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public CustomAuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    // Used by spring security if CORS is enabled.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
