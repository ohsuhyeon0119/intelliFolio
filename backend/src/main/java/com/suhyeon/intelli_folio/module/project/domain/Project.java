package com.suhyeon.intelli_folio.module.project.domain;


import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
class Project {
    private Long id;
    private Long userId;
    private String name;
    private String summary;
    private Instant createdAt;
    private Instant updatedAt;
}
