package com.loginriskmonitor.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "login_attempts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Stores the submitted username even if no matching user exists.
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "successful", nullable = false)
    private boolean successful;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false, length = 20)
    private RiskLevel riskLevel;

    @Column(name = "attempted_at", nullable = false)
    private LocalDateTime attemptedAt;

    public LoginAttempt(String username,
                        String ipAddress,
                        boolean successful,
                        RiskLevel riskLevel,
                        LocalDateTime attemptedAt) {
        this.username = username;
        this.ipAddress = ipAddress;
        this.successful = successful;
        this.riskLevel = riskLevel;
        this.attemptedAt = attemptedAt;
    }
}
