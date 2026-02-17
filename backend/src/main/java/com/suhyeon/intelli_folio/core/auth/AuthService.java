package com.suhyeon.intelli_folio.core.auth;


import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import io.jsonwebtoken.security.Keys;

@Service
public class AuthService {
    private final AuthProperties props;

    public AuthService(AuthProperties props) {
        this.props = props;
    }

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



}
