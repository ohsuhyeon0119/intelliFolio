package com.suhyeon.intelli_folio.module.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateUserCommand {
    private Long id;
    private String email;
    private String passwordHash;
    private String displayName;
}
