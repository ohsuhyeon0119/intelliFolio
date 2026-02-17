package com.suhyeon.intelli_folio.module.project.dto;

public record UploadedDocumentResult(
        long documentId,
        String filename,
        int chunksInserted
) {}