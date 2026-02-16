package com.suhyeon.intelli_folio.rag.qdrant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class QdrantClient {

    private final WebClient qdrantWebClient;
    private final QdrantProperties properties;

    public void ensureCollectionExists() {
        String collectionName = properties.getCollection();

        try {
            qdrantWebClient
                    .get()
                    .uri("/collections/{name}", collectionName)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("Collection already exists.");

        } catch (Exception e) {
            System.out.println("Collection not found. Creating...");

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

        System.out.println("Collection created.");
    }
}