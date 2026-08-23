package com.loginriskmonitor.service;

import com.loginriskmonitor.dto.DashboardStatsReadOnlyDTO;
import com.loginriskmonitor.domain.LoginAttempt;
import com.loginriskmonitor.domain.RiskLevel;
import com.loginriskmonitor.mapper.LoginAttemptMapper;
import com.loginriskmonitor.repository.LoginAttemptRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginAttemptServiceTest {

    @Mock
    private LoginAttemptRepository loginAttemptRepository;

    @Mock
    private LoginAttemptMapper loginAttemptMapper;

    @InjectMocks
    private LoginAttemptService loginAttemptService;

    @Test
    void successfulAttemptShouldHaveLowRisk() {
        loginAttemptService.recordAttempt(
                "admin",
                "127.0.0.1",
                true
        );

        LoginAttempt savedAttempt = captureSavedAttempt();

        assertEquals("admin", savedAttempt.getUsername());
        assertEquals("127.0.0.1", savedAttempt.getIpAddress());
        assertTrue(savedAttempt.isSuccessful());
        assertEquals(RiskLevel.LOW, savedAttempt.getRiskLevel());
    }

    @Test
    void firstFailedAttemptShouldHaveMediumRisk() {
        when(loginAttemptRepository
                .countByUsernameAndSuccessfulFalse("admin"))
                .thenReturn(0L);

        loginAttemptService.recordAttempt(
                "admin",
                "127.0.0.1",
                false
        );

        LoginAttempt savedAttempt = captureSavedAttempt();

        assertFalse(savedAttempt.isSuccessful());
        assertEquals(RiskLevel.MEDIUM, savedAttempt.getRiskLevel());
    }

    @Test
    void secondFailedAttemptShouldHaveMediumRisk() {
        when(loginAttemptRepository
                .countByUsernameAndSuccessfulFalse("admin"))
                .thenReturn(1L);

        loginAttemptService.recordAttempt(
                "admin",
                "127.0.0.1",
                false
        );

        LoginAttempt savedAttempt = captureSavedAttempt();

        assertFalse(savedAttempt.isSuccessful());
        assertEquals(RiskLevel.MEDIUM, savedAttempt.getRiskLevel());
    }

    @Test
    void thirdFailedAttemptShouldHaveHighRisk() {
        when(loginAttemptRepository
                .countByUsernameAndSuccessfulFalse("admin"))
                .thenReturn(2L);

        loginAttemptService.recordAttempt(
                "admin",
                "127.0.0.1",
                false
        );

        LoginAttempt savedAttempt = captureSavedAttempt();

        assertFalse(savedAttempt.isSuccessful());
        assertEquals(RiskLevel.HIGH, savedAttempt.getRiskLevel());
    }

    @Test
    void blankAuditValuesShouldBeNormalized() {
        loginAttemptService.recordAttempt("   ", null, true);

        LoginAttempt savedAttempt = captureSavedAttempt();

        assertEquals("unknown", savedAttempt.getUsername());
        assertEquals("unknown", savedAttempt.getIpAddress());
    }

    @Test
    void shouldReturnOverallDashboardStats() {
        when(loginAttemptRepository.count())
                .thenReturn(30L);

        when(loginAttemptRepository.countBySuccessfulTrue())
                .thenReturn(22L);

        when(loginAttemptRepository.countBySuccessfulFalse())
                .thenReturn(8L);

        when(loginAttemptRepository.countByRiskLevel(RiskLevel.HIGH))
                .thenReturn(3L);

        DashboardStatsReadOnlyDTO stats =
                loginAttemptService.getDashboardStats();

        assertEquals(30L, stats.totalAttempts());
        assertEquals(22L, stats.successfulAttempts());
        assertEquals(8L, stats.failedAttempts());
        assertEquals(3L, stats.highRiskAttempts());
    }

    @Test
    void shouldReturnDashboardStatsForSpecificUser() {
        when(loginAttemptRepository.countByUsername("user"))
                .thenReturn(7L);

        when(loginAttemptRepository
                .countByUsernameAndSuccessfulTrue("user"))
                .thenReturn(5L);

        when(loginAttemptRepository
                .countByUsernameAndSuccessfulFalse("user"))
                .thenReturn(2L);

        when(loginAttemptRepository
                .countByUsernameAndRiskLevel("user", RiskLevel.HIGH))
                .thenReturn(1L);

        DashboardStatsReadOnlyDTO stats =
                loginAttemptService
                        .getDashboardStatsByUsername("user");

        assertEquals(7L, stats.totalAttempts());
        assertEquals(5L, stats.successfulAttempts());
        assertEquals(2L, stats.failedAttempts());
        assertEquals(1L, stats.highRiskAttempts());
    }

    private LoginAttempt captureSavedAttempt() {
        ArgumentCaptor<LoginAttempt> captor =
                ArgumentCaptor.forClass(LoginAttempt.class);

        verify(loginAttemptRepository).save(captor.capture());

        return captor.getValue();
    }
}
