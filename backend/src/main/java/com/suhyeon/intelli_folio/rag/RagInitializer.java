package com.suhyeon.intelli_folio.rag;

import com.suhyeon.intelli_folio.rag.ollama.EmbeddingClient;
import com.suhyeon.intelli_folio.rag.qdrant.QdrantClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RagInitializer {

    private final QdrantClient qdrantClient;
    private  final EmbeddingClient embeddingClient;
    @PostConstruct
    public void init() {
        // qdrant
        qdrantClient.ensureCollectionExists();
        // embedding
        List<Float> vec = embeddingClient.embedOne("hello, world!");
        System.out.println("embedding size = " + vec.size());
        System.out.println("first3 = " + vec.subList(0, Math.min(3, vec.size())));
    }


}
