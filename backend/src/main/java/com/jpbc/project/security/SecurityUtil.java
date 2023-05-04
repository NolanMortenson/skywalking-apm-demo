package com.jpbc.project.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    private static final String SECRET = "encryption_secret!@#$%^&(";
    public static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET.getBytes());

    public static String getUsernameFromJWT(String token) {
        try {
            return decodeJWT(token).getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    public static DecodedJWT decodeJWT(String token) {
        return JWT.require(ALGORITHM).build().verify(token);
    }

    public static boolean checkAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() != SecurityConfig.ANONYMOUS_USER;
    }
}
