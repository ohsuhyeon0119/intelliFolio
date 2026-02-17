package com.suhyeon.intelli_folio.module.project.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter
public class Document {
    private Long id;
    private Long projectId;
    private String title;
    private String originalFilename;
    private String contentType;
    private Long sizeBytes;
    private Instant createdAt;
    private Instant updatedAt;
}