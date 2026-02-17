package com.suhyeon.intelli_folio.core.auth;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthProperties props;

    public String issueAccessToken(long userId, String email) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(props.getAccessTokenSeconds());


        var key = Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .issuer(props.getIssuer())
                .subject(Long.toString(userId))
                .claim("email", email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key)
                .compact();
    }


    public VerifiedToken verifyFromRequest(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = extractBearerToken(header);
        if (token == null) return null; // 토큰이 없으면 null (필터에서 "필수/선택" 처리)

        return verify(token);
    }


    public VerifiedToken verify(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8));

            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .requireIssuer(props.getIssuer())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            long userId = Long.parseLong(claims.getSubject());
            String email = claims.get("email", String.class);

            return new VerifiedToken(userId, email);

        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("Invalid JWT", e);
        }
    }

    private String extractBearerToken(String header) {
        if (header == null) return null;
        if (!header.startsWith("Bearer ")) return null;
        String token = header.substring("Bearer ".length()).trim();
        return token.isEmpty() ? null : token;
    }

    public record VerifiedToken(long userId, String email) {}


    public static class InvalidTokenException extends RuntimeException {
        public InvalidTokenException(String message, Throwable cause) {
            super(message, cause);
        }
    }



}
