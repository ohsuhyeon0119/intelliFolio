package com.suhyeon.intelli_folio.rag;

import com.suhyeon.intelli_folio.rag.ollama.EmbeddingClient;
import com.suhyeon.intelli_folio.rag.qdrant.QdrantClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RagInitializer {
    private static final Logger log = LoggerFactory.getLogger(RagInitializer.class);
    private final QdrantClient qdrantClient;
    private  final EmbeddingClient embeddingClient;
    @PostConstruct
    public void init() {
        // qdrant
        qdrantClient.ensureCollectionExists();
        // embedding
        List<Float> vec = embeddingClient.embedOne("hello, world!");
        log.info("embedding server heath check");
        log.info(String.format("embedding size= %d",vec.size()) );
    }


}
