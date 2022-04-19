package com.project.devblog.config;

import com.project.devblog.model.enums.Role;
import com.project.devblog.security.JwtTokenAuthorizationFilter;
import com.project.devblog.security.RestAccessDeniedHandler;
import com.project.devblog.security.RestAuthenticationEntryPoint;
import com.project.devblog.security.oauth2.CustomOAuth2UserService;
import com.project.devblog.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.project.devblog.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.project.devblog.security.oauth2.OAuth2AuthenticationSuccessHandler;
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
@EnableWebSecurity/*(debug = true)*/
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] AUTH_URL = {
            "/v1/auth/**",
            "/v1/oauth2/**",
            "/login",
            "/auth/**",
            "/oauth2/**",
            "/"};
    private static final String[] OPEN_API_URLS = {
            "/api-docs/**",
            "/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/actuator/**",
            "/management/**"
    };
    private final JwtTokenAuthorizationFilter jwtTokenAuthorizationFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.exceptionHandling()
//                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler());

        http.authorizeRequests()
                .antMatchers(OPEN_API_URLS).permitAll()
                .antMatchers(AUTH_URL).permitAll()
                .antMatchers("/v1/articles/**").permitAll()
                .antMatchers("/v1/tags/**").hasAuthority(Role.ADMIN.toString())
                .antMatchers("/v1/users/**").hasAnyAuthority(Role.USER.toString(), Role.ADMIN.toString())
                .anyRequest().authenticated();

        http.oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oauth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);

        http.addFilterBefore(jwtTokenAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }


    /*
    Реализация по умолчанию AuthorizationRequestRepository — HttpSessionOAuth2AuthorizationRequestRepository,
        которая хранит OAuth2AuthorizationRequest в файле HttpSession.
    Но наш сервис является STATELESS, поэтому мы не можем использовать сессии.
    Поэтому мы будем сохранять запросы в закодированных с помощью Base64 куки файлах.
    Для этого мы предоставим пользовательскую реализацию AuthorizationRequestRepository,
        которая хранит атрибуты OAuth2AuthorizationRequest в Cookie, настройте ее
    */
    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public RestAccessDeniedHandler accessDeniedHandler() {
        return new RestAccessDeniedHandler();
    }

    @Bean
    public RestAuthenticationEntryPoint authenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

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
