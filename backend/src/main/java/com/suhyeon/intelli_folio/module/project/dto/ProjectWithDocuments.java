package com.suhyeon.intelli_folio.module.project.dto;

import java.util.List;

public record ProjectWithDocuments(
        long id,
        String name,
        String summary,
        List<DocumentSummary> documents
) {}

