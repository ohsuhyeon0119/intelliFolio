package com.suhyeon.intelli_folio.rag.ollama;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class EmbeddingConfig {

    @Bean
    public WebClient embeddingWebClient(EmbeddingProperties props) {
        return WebClient.builder()
                .baseUrl(props.getBaseUrl())
                .build();
    }
}