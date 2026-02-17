package com.suhyeon.intelli_folio.core.rag.qdrant;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PointIdBuilder {
    public static String forChunk(long userId, long projectId, long docId, long chunkId) {
        String raw = userId + ":" + projectId + ":" + docId + ":" + chunkId;
        return UUID.nameUUIDFromBytes(raw.getBytes(StandardCharsets.UTF_8)).toString();
    }
}
