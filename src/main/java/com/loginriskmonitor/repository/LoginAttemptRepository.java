package com.loginriskmonitor.repository;

import com.loginriskmonitor.domain.LoginAttempt;
import com.loginriskmonitor.domain.RiskLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoginAttemptRepository
        extends JpaRepository<LoginAttempt, Long> {

    List<LoginAttempt> findAllByOrderByAttemptedAtDesc();

    List<LoginAttempt> findByUsernameOrderByAttemptedAtDesc(
            String username
    );

    long countBySuccessfulTrue();

    long countBySuccessfulFalse();

    long countByRiskLevel(RiskLevel riskLevel);

    long countByUsername(String username);

    long countByUsernameAndSuccessfulTrue(String username);

    long countByUsernameAndSuccessfulFalse(String username);

    long countByUsernameAndRiskLevel(
            String username,
            RiskLevel riskLevel
    );
}
