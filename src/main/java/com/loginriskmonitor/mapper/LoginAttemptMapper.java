package com.loginriskmonitor.mapper;

import com.loginriskmonitor.domain.LoginAttempt;
import com.loginriskmonitor.dto.LoginAttemptReadOnlyDTO;
import org.springframework.stereotype.Component;

@Component
public class LoginAttemptMapper {

    public LoginAttemptReadOnlyDTO toReadOnlyDTO(LoginAttempt loginAttempt) {
        return new LoginAttemptReadOnlyDTO(
                loginAttempt.getUsername(),
                loginAttempt.getIpAddress(),
                loginAttempt.isSuccessful(),
                loginAttempt.getRiskLevel().name(),
                loginAttempt.getAttemptedAt()
        );
    }
}
