package com.suhyeon.intelli_folio.module.project.dto;

import java.util.List;

public record UploadDocumentsResponse(
        long projectId,
        List<UploadedDocumentResult> documents
) {}