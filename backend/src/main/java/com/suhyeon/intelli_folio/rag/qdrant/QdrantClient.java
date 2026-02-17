package com.suhyeon.intelli_folio.rag.qdrant;

import com.suhyeon.intelli_folio.rag.RagInitializer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class QdrantClient {

    private final WebClient qdrantWebClient;
    private final QdrantProperties properties;
    private static final Logger log = LoggerFactory.getLogger(QdrantClient.class);

    public void ensureCollectionExists() {
        String collectionName = properties.getCollection();

        try {
            qdrantWebClient
                    .get()
                    .uri("/collections/{name}", collectionName)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("Collection already exists.");

        } catch (Exception e) {
            log.info("Collection not found. Creating...");

            createCollection(collectionName);
        }
    }

    private void createCollection(String collectionName) {

        Map<String, Object> body = Map.of(
                "vectors", Map.of(
                        "size", properties.getVectorSize(),
                        "distance", "Cosine"
                )
        );

        qdrantWebClient
                .put()
                .uri("/collections/{name}", collectionName)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("Collection created.");
    }
}