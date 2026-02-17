package com.suhyeon.intelli_folio.module.user.domain;

import java.time.Instant;

public record User(
        Long id,
        String email,
        String passwordHash,
        String displayName,
        Instant createdAt,
        Instant updatedAt
) {}