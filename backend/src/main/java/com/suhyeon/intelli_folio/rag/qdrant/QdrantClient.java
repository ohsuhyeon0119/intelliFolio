package com.suhyeon.intelli_folio.rag.qdrant;

import com.suhyeon.intelli_folio.rag.RagInitializer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public void upsert(String collection, String pointId, List<Float> vector, Map<String, Object> payload) {
        var req = new UpsertRequest(
                List.of(new Point(pointId, vector, payload)),
                true
        );

        qdrantWebClient.put()
                .uri("/collections/{collection}/points?wait=true", collection)
                .bodyValue(req)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }

    public List<SearchHit> search(
            String collection,
            List<Float> vector,
            SearchScope scope,
            int limit
    ) {

        var filter = buildSearchFilter(scope);

        Map<String, Object> request = new HashMap<>();
        request.put("vector", vector);
        request.put("limit", limit);
        request.put("with_payload", true);

        if (filter != null) {
            request.put("filter", filter);
        }

        var response = qdrantWebClient.post()
                .uri("/collections/{collection}/points/search", collection)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(SearchResponse.class)
                .block();

        if (response == null || response.result() == null) {
            return List.of();
        }

        return response.result().stream()
                .map(r -> new SearchHit(r.id(), r.score(), r.payload()))
                .toList();
    }








    private Map<String, Object> buildSearchFilter(SearchScope scope) {
        List<Map<String, Object>> must = new ArrayList<>();

        if (scope.userId() != null) {
            must.add(Map.of(
                    "key", "user_id",
                    "match", Map.of("value", scope.userId())
            ));
        }

        if (scope.projectId() != null) {
            must.add(Map.of(
                    "key", "project_id",
                    "match", Map.of("value", scope.projectId())
            ));
        }

        if (scope.docId() != null) {
            must.add(Map.of(
                    "key", "doc_id",
                    "match", Map.of("value", scope.docId())
            ));
        }

        if (must.isEmpty()) {
            return null; // 필터 없음
        }

        return Map.of("must", must);
    }





    public record UpsertRequest(List<Point> points, boolean wait_flag) {}
    public record Point(String id, List<Float> vector, Map<String, Object> payload) {}

    public record SearchScope(
            Long userId,
            Long projectId,
            Long docId
    ) {}
    public record SearchHit(String id, double score, Map<String, Object> payload) {}
    public record SearchResponse(List<SearchResult> result) {}
    public record SearchResult(String id, double score, Map<String, Object> payload) {}
}
}