package com.suhyeon.intelli_folio.module.rag;

import com.suhyeon.intelli_folio.core.rag.ollama.EmbeddingClient;
import com.suhyeon.intelli_folio.core.rag.qdrant.QdrantClient;
import com.suhyeon.intelli_folio.core.rag.qdrant.QdrantClient.SearchScope;
import com.suhyeon.intelli_folio.core.rag.qdrant.QdrantClient.SearchHit;

import com.suhyeon.intelli_folio.core.rag.qdrant.QdrantProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RagService {

    private final EmbeddingClient embeddingClient; // 너가 만들 것
    private final QdrantClient qdrantClient;
    private final QdrantProperties qdrantProperties;

    /**
     * chunkText를 임베딩하고, payload와 함께 Qdrant에 저장
     */
    public void insertDataToVectorDB(
            String pointId,
            long userId,
            long projectId,
            long docId,
            long chunkId,
            String fileName,
            String chunkText
    ) {
        // 1) embed
        List<Float> vector = embeddingClient.embedOne(chunkText);

        // 2) payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("user_id", userId);
        payload.put("project_id", projectId);
        payload.put("doc_id", docId);
        payload.put("chunk_id", chunkId);
        payload.put("file_name", fileName);
        payload.put("text", chunkText); // 원문도 같이 저장하고 싶으면

        // 3) upsert
        qdrantClient.upsert(qdrantProperties.getCollection(), pointId, vector, payload);
    }



    /**
     * query를 임베딩하고, scope로 필터 걸어 Qdrant 검색
     */
    public List<SearchHit> search(
            String query,
            SearchScope scope,
            int limit
    ) {
        // 1) embed query
        List<Float> vector = embeddingClient.embedOne(query);

        // 2) search and return
        return qdrantClient.search(qdrantProperties.getCollection(), vector, scope, limit);
    }
}
