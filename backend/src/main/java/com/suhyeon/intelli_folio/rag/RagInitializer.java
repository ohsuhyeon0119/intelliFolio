package com.suhyeon.intelli_folio.rag;

import com.suhyeon.intelli_folio.rag.qdrant.QdrantClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RagInitializer {

    private final QdrantClient qdrantClient;

    @PostConstruct
    public void init() {
        qdrantClient.ensureCollectionExists();
    }
}
