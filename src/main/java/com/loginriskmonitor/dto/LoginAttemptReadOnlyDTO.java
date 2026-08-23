package com.loginriskmonitor.dto;

import java.time.LocalDateTime;

public record LoginAttemptReadOnlyDTO(
        String username,
        String ipAddress,
        boolean successful,
        String riskLevel,
        LocalDateTime attemptedAt
) {
}