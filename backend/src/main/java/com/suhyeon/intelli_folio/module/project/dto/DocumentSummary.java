package com.suhyeon.intelli_folio.module.project.dto;

public  record DocumentSummary(
        long id,
        String title,
        String originalFilename,
        Long sizeBytes
) {}
