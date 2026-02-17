package com.suhyeon.intelli_folio.core.auth;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "auth.jwt")
@Getter
@Setter
public class AuthProperties {
    String secret;
    String issuer;
    long accessTokenSeconds;
}
