package com.suhyeon.intelli_folio.rag.ollama;

import com.suhyeon.intelli_folio.rag.ollama.dto.EmbeddingRequest;
import com.suhyeon.intelli_folio.rag.ollama.dto.EmbeddingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EmbeddingClient {

    private final WebClient embeddingWebClient;
    private final EmbeddingProperties props;

    public List<Float> embedOne(String text) {
        EmbeddingResponse res = embeddingWebClient.post()
                .uri("/v1/embeddings")
                .bodyValue(EmbeddingRequest.ofText(text, props.getModel()))
                .retrieve()
                .bodyToMono(EmbeddingResponse.class)
                .timeout(Duration.ofMillis(props.getTimeoutMs()))
                .block();

        if (res == null || res.data() == null || res.data().isEmpty()) {
            throw new IllegalStateException("Empty embedding response");
        }
        return res.data().get(0).embedding();
    }

    public List<List<Float>> embedMany(List<String> texts) {
        EmbeddingResponse res = embeddingWebClient.post()
                .uri("/v1/embeddings")
                .bodyValue(EmbeddingRequest.ofTexts(texts, props.getModel()))
                .retrieve()
                .bodyToMono(EmbeddingResponse.class)
                .timeout(Duration.ofMillis(props.getTimeoutMs()))
                .block();

        if (res == null || res.data() == null || res.data().isEmpty()) {
            throw new IllegalStateException("Empty embedding response");
        }
        return res.data().stream().map(EmbeddingResponse.EmbeddingData::embedding).toList();
    }
}