package com.loginriskmonitor.dto;

public record UserReadOnlyDTO(
        Long id,
        String username,
        String role
) {
}
