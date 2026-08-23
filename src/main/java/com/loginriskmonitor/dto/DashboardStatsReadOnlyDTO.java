package com.loginriskmonitor.dto;

public record DashboardStatsReadOnlyDTO(
        long totalAttempts,
        long successfulAttempts,
        long failedAttempts,
        long highRiskAttempts
) {
}