package com.loginriskmonitor.mapper;

import com.loginriskmonitor.domain.LoginAttempt;
import com.loginriskmonitor.dto.LoginAttemptReadOnlyDTO;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class LoginAttemptMapper {

    private static final ZoneId ATHENS_ZONE = ZoneId.of("Europe/Athens");

    public LoginAttemptReadOnlyDTO toReadOnlyDTO(LoginAttempt loginAttempt) {
        return new LoginAttemptReadOnlyDTO(
                loginAttempt.getUsername(),
                loginAttempt.getIpAddress(),
                loginAttempt.isSuccessful(),
                loginAttempt.getRiskLevel().name(),
                loginAttempt.getAttemptedAt().atZone(ATHENS_ZONE)
        );
    }
}