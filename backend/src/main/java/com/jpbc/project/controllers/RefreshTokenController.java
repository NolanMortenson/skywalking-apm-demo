package com.jpbc.project.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpbc.project.models.TeamMember;
import com.jpbc.project.repos.TeamMemberRepo;
import com.jpbc.project.security.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jpbc.project.filters.CustomAuthenticationFilter.EXPIRATION_TOKEN;
import static com.jpbc.project.filters.CustomAuthenticationFilter.REFRESH_COOKIE_NAME;
import static com.jpbc.project.filters.CustomAuthenticationFilter.REFRESH_COOKIE_PATH;
import static com.jpbc.project.filters.CustomAuthorizationFilter.EXPIRED;
import static com.jpbc.project.security.SecurityConfig.ROLES;
import static com.jpbc.project.security.SecurityUtil.ALGORITHM;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Controller
@RequestMapping("/api/v1/token/")
@CrossOrigin
@AllArgsConstructor
public class RefreshTokenController {
    static final String EXPIRED_SESSION_MSG = "Session has expired please login";
    static final String TOKEN = "token";
    static final String ERROR_PREFIX = "Error: ";

    private final TeamMemberRepo teamMemberRepo;

    @GetMapping("refresh")
    public void refreshToken(@NotNull HttpServletRequest request, HttpServletResponse response) throws IOException {

        Cookie[] allCookies = request.getCookies();
        String refreshToken = null;

        // Iterate through all cookies, only use valid cookie.
        for (Cookie cookie : allCookies) {
            DecodedJWT jwt = JWT.decode(cookie.getValue());
            if (jwt.getExpiresAt().before(new Date())) {
                continue;
            }
            refreshToken = cookie.getValue();
        }

        if (refreshToken != null) {
            try {
                String username = SecurityUtil.getUsernameFromJWT(refreshToken);
                TeamMember teamMember = teamMemberRepo.findByEmail(username).orElse(null);
                assert teamMember != null;
                Stream<String> s = Stream.of(teamMember.getRole().toString());
                List<?> authorities = s.collect(Collectors.toList());
                String access_token = JWT.create()
                        .withSubject(teamMember.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TOKEN))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim(ROLES, authorities)
                        .sign(ALGORITHM);
                Map<String, Object> refresh_token_return = new HashMap<>();
                refresh_token_return.put(TOKEN, access_token);
                response.setContentType(APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(response.getOutputStream(), refresh_token_return);

            } catch (TokenExpiredException exception2) {
                log.error(ERROR_PREFIX + exception2.getMessage());
                response.setStatus(UNAUTHORIZED.value());
                Map<String, String> error = new HashMap<>();
                error.put(EXPIRED, exception2.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(response.getOutputStream(), error);

            } catch (Exception exception) {
                log.error(ERROR_PREFIX + exception.getMessage());
                response.setStatus(FORBIDDEN.value());
            }
        } else {
            throw new RuntimeException(EXPIRED_SESSION_MSG);
        }
    }

    @GetMapping("/logout")
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(REFRESH_COOKIE_NAME, null);
        cookie.isHttpOnly();
        cookie.setPath(REFRESH_COOKIE_PATH);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
