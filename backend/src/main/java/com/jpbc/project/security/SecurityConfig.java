package com.jpbc.project.security;

import com.jpbc.project.filters.CustomAuthenticationFilter;
import com.jpbc.project.filters.CustomAuthorizationFilter;
import com.jpbc.project.repos.TeamMemberRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String LOGIN_PATH = "/login";
    public static final String ADMIN_AUTHORITY = "ADMIN";
    public static final String USER_AUTHORITY = "USER";
    public static final String ROLES = "roles";
    public static final String TOKEN_PATH = "/api/v1/token";

    static final String GET_PROJECT_PATH = "/api/v1/projects/**";
    static final String ANONYMOUS_USER = "anonymousUser";
    static final String REFRESH_PATH = TOKEN_PATH + "/refresh";
    static final String LOGOUT_PATH = TOKEN_PATH + "/logout";
    static final String BASE_URL = "http://localhost:3000";
    static final String X_AUTH_TOKEN = "x-auth-token";
    static final String CORS_PATTERN = "/**";

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final TeamMemberRepo teamMemberRepo;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers(LOGIN_PATH, REFRESH_PATH).permitAll();
        http.cors().and().authorizeRequests().antMatchers(LOGIN_PATH, REFRESH_PATH).permitAll();
        http.authorizeRequests().antMatchers(GET, GET_PROJECT_PATH).permitAll();
        http.authorizeRequests().antMatchers(GET, LOGOUT_PATH).permitAll();
        http.authorizeRequests().antMatchers(POST).hasAuthority(ADMIN_AUTHORITY);
        http.authorizeRequests().antMatchers(PUT).hasAuthority(ADMIN_AUTHORITY);
        http.authorizeRequests().antMatchers(DELETE).hasAuthority(ADMIN_AUTHORITY);
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean(), teamMemberRepo));
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(BASE_URL));
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(
                Arrays.asList(
                        GET.toString(),
                        POST.toString(),
                        PUT.toString(),
                        PATCH.toString(),
                        DELETE.toString(),
                        OPTIONS.toString()
                )
        );
        configuration.setAllowedHeaders(
                Arrays.asList(
                        HttpHeaders.AUTHORIZATION,
                        HttpHeaders.CONTENT_TYPE,
                        X_AUTH_TOKEN
                )
        );
        configuration.setExposedHeaders(List.of(X_AUTH_TOKEN));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(CORS_PATTERN, configuration);
        return source;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
