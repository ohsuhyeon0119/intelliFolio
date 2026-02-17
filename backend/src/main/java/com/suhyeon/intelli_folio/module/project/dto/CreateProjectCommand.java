package com.suhyeon.intelli_folio.module.project.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProjectCommand {
    private Long id;       // generated key will be set here
    private Long userId;
    private String name;
    private String summary;
}