package com.suhyeon.intelli_folio.rag.ollama;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "embedding")
public class EmbeddingProperties {
    private String baseUrl;
    private String model;
    private int timeoutMs;
}