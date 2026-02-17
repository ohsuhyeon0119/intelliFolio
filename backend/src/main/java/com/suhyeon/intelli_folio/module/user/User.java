package com.suhyeon.intelli_folio.module.user;

import java.time.Instant;

public record User(
        Long id,
        String email,
        String passwordHash,
        String displayName,
        Instant createdAt,
        Instant updatedAt
) {}