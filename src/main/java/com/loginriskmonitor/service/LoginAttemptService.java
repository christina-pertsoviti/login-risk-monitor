package com.loginriskmonitor.service;

import com.loginriskmonitor.dto.DashboardStatsReadOnlyDTO;
import com.loginriskmonitor.dto.LoginAttemptReadOnlyDTO;
import com.loginriskmonitor.domain.LoginAttempt;
import com.loginriskmonitor.domain.RiskLevel;
import com.loginriskmonitor.mapper.LoginAttemptMapper;
import com.loginriskmonitor.repository.LoginAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginAttemptService implements ILoginAttemptService {

    private static final String UNKNOWN_VALUE = "unknown";
    private static final int MAX_USERNAME_LENGTH = 50;
    private static final int MAX_IP_ADDRESS_LENGTH = 45;

    private final LoginAttemptRepository loginAttemptRepository;
    private final LoginAttemptMapper loginAttemptMapper;

    @Override
    @Transactional
    public void recordAttempt(
            String username,
            String ipAddress,
            boolean successful
    ) {
        String normalizedUsername = normalizeAuditValue(
                username,
                MAX_USERNAME_LENGTH
        );
        String normalizedIpAddress = normalizeAuditValue(
                ipAddress,
                MAX_IP_ADDRESS_LENGTH
        );
        RiskLevel riskLevel = calculateRiskLevel(
                normalizedUsername,
                successful
        );

        LoginAttempt loginAttempt = new LoginAttempt(
                normalizedUsername,
                normalizedIpAddress,
                successful,
                riskLevel,
                LocalDateTime.now()
        );

        loginAttemptRepository.save(loginAttempt);
    }

    @Override
    public List<LoginAttemptReadOnlyDTO> getAllAttempts() {
        return loginAttemptRepository
                .findAllByOrderByAttemptedAtDesc()
                .stream()
                .map(loginAttemptMapper::toReadOnlyDTO)
                .toList();
    }

    @Override
    public List<LoginAttemptReadOnlyDTO> getAttemptsByUsername(
            String username
    ) {
        return loginAttemptRepository
                .findByUsernameOrderByAttemptedAtDesc(username)
                .stream()
                .map(loginAttemptMapper::toReadOnlyDTO)
                .toList();
    }

    @Override
    public DashboardStatsReadOnlyDTO getDashboardStats() {
        long totalAttempts = loginAttemptRepository.count();

        long successfulAttempts =
                loginAttemptRepository.countBySuccessfulTrue();

        long failedAttempts =
                loginAttemptRepository.countBySuccessfulFalse();

        long highRiskAttempts =
                loginAttemptRepository.countByRiskLevel(RiskLevel.HIGH);

        return new DashboardStatsReadOnlyDTO(
                totalAttempts,
                successfulAttempts,
                failedAttempts,
                highRiskAttempts
        );
    }

    @Override
    public DashboardStatsReadOnlyDTO getDashboardStatsByUsername(
            String username
    ) {
        long totalAttempts =
                loginAttemptRepository.countByUsername(username);

        long successfulAttempts =
                loginAttemptRepository
                        .countByUsernameAndSuccessfulTrue(username);

        long failedAttempts =
                loginAttemptRepository
                        .countByUsernameAndSuccessfulFalse(username);

        long highRiskAttempts =
                loginAttemptRepository
                        .countByUsernameAndRiskLevel(
                                username,
                                RiskLevel.HIGH
                        );

        return new DashboardStatsReadOnlyDTO(
                totalAttempts,
                successfulAttempts,
                failedAttempts,
                highRiskAttempts
        );
    }

    private RiskLevel calculateRiskLevel(
            String username,
            boolean successful
    ) {
        if (successful) {
            return RiskLevel.LOW;
        }

        long failedAttempts =
                loginAttemptRepository
                        .countByUsernameAndSuccessfulFalse(username);

        if (failedAttempts >= 2) {
            return RiskLevel.HIGH;
        }

        return RiskLevel.MEDIUM;
    }

    private String normalizeAuditValue(String value, int maxLength) {
        if (value == null || value.isBlank()) {
            return UNKNOWN_VALUE;
        }

        String normalizedValue = value.strip();
        return normalizedValue.length() <= maxLength
                ? normalizedValue
                : normalizedValue.substring(0, maxLength);
    }
}
