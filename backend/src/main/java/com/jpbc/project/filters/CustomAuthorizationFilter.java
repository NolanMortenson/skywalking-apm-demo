package com.jpbc.project.filters;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpbc.project.security.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.jpbc.project.security.SecurityConfig.LOGIN_PATH;
import static com.jpbc.project.security.SecurityConfig.ROLES;
import static com.jpbc.project.security.SecurityConfig.TOKEN_PATH;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    public static final String EXPIRED = "expired";

    static final String BEARER = "Bearer ";
    static final String LOGIN_ERROR = "Error Logging in: {}";
    static final String REFRESH_PATH = TOKEN_PATH + "/refresh";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        if (request.getServletPath().equals(LOGIN_PATH) || request.getServletPath().equals(REFRESH_PATH)) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {
                try {
                    String token = authorizationHeader.substring(BEARER.length());
                    String username = SecurityUtil.getUsernameFromJWT(token);
                    String[] roles = SecurityUtil.decodeJWT(token).getClaim(ROLES).asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(roles[0]));
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);

                } catch (TokenExpiredException exception) {
                    log.error(LOGIN_ERROR, exception.getMessage());
                    response.setStatus(UNPROCESSABLE_ENTITY.value());
                    Map<String, String> error = new HashMap<>();
                    error.put(EXPIRED, exception.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);

                } catch (Exception exception) {
                    log.error(LOGIN_ERROR, exception.getMessage());
                    response.setStatus(FORBIDDEN.value());
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
