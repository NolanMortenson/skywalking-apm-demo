package com.jpbc.project.filters;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpbc.project.models.TeamMember;
import com.jpbc.project.repos.TeamMemberRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.jpbc.project.security.SecurityConfig.ADMIN_AUTHORITY;
import static com.jpbc.project.security.SecurityConfig.ROLES;
import static com.jpbc.project.security.SecurityUtil.ALGORITHM;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public static final int EXPIRATION_TOKEN = 10 * 60 * 1000;
    public static final int REFRESH_EXPIRATION = 10 * 60 * 60 * 1000;
    public static final String REFRESH_COOKIE_NAME = "refresh";
    public static final String REFRESH_COOKIE_PATH = "/";

    static final String ID = "ID";
    static final String IS_ADMIN = "isAdmin";
    static final String FIRST_NAME = "firstName";
    static final String LAST_NAME = "lastName";
    static final String EMAIL = "email";
    static final String TOKEN = "token";
    static final String USERNAME = "username";
    static final String PASSWORD = "password";

    private final AuthenticationManager authenticationManager;
    private final TeamMemberRepo teamMemberRepo;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, TeamMemberRepo teamMemberRepo) {
        this.authenticationManager = authenticationManager;
        this.teamMemberRepo = teamMemberRepo;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getParameter(USERNAME), request.getParameter(PASSWORD));
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authentication
    ) throws IOException {
        User user = (User) authentication.getPrincipal();
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TOKEN))
                .withIssuer(request.getRequestURL().toString())
                .withClaim(ROLES, user
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
                )
                .sign(ALGORITHM);

        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
                .withIssuer(request.getRequestURL().toString())
                .sign(ALGORITHM);

        Map<String, Object> loginReturn = new HashMap<>();
        loginReturn.put(TOKEN, accessToken);
        loginReturn.put(EMAIL, user.getUsername());

        String role = authentication.getAuthorities().toArray()[0].toString();

        TeamMember loggedInUser = teamMemberRepo.findByEmail(user.getUsername()).orElse(null);

        if (loggedInUser != null) {
            loginReturn.put(ID, loggedInUser.getId());
            loginReturn.put(FIRST_NAME, loggedInUser.getFirstName());
            loginReturn.put(LAST_NAME, loggedInUser.getLastName());
            loginReturn.put(IS_ADMIN, Objects.equals(role, ADMIN_AUTHORITY));
        }

        response.setContentType(APPLICATION_JSON_VALUE);
        Cookie cookie = new Cookie(REFRESH_COOKIE_NAME, refreshToken);
        cookie.isHttpOnly();
        cookie.setPath(REFRESH_COOKIE_PATH);
        cookie.setMaxAge(REFRESH_EXPIRATION);
        response.addCookie(cookie);

        new ObjectMapper().writeValue(response.getOutputStream(), loginReturn);
    }

}

