package com.suhyeon.intelli_folio.rag.ollama.dto;

import java.util.List;

public record EmbeddingResponse(
        List<EmbeddingData> data,
        String model
) {
    public record EmbeddingData(
            List<Float> embedding,
            int index
    ) {}
}