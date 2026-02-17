package com.suhyeon.intelli_folio.module.project.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDocumentCommand {
    private Long id;
    private Long projectId;
    private String title;
    private String originalFilename;
    private String contentType;
    private Long sizeBytes;
}