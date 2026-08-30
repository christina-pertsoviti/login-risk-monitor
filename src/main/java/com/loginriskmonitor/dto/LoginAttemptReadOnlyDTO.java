package com.loginriskmonitor.dto;

import java.time.ZonedDateTime;

public record LoginAttemptReadOnlyDTO(
        String username,
        String ipAddress,
        boolean successful,
        String riskLevel,
        ZonedDateTime attemptedAt
) {
}