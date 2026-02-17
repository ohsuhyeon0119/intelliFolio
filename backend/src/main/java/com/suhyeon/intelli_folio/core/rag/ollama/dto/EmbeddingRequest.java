package com.suhyeon.intelli_folio.core.rag.ollama.dto;

import java.util.List;

public record EmbeddingRequest(
        Object input,
        String model
) {
    public static EmbeddingRequest ofTexts(List<String> texts, String model) {
        return new EmbeddingRequest(texts, model);
    }

    public static EmbeddingRequest ofText(String text, String model) {
        return new EmbeddingRequest(text, model);
    }
}