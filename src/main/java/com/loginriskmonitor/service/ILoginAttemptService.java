package com.loginriskmonitor.service;

import com.loginriskmonitor.dto.DashboardStatsReadOnlyDTO;
import com.loginriskmonitor.dto.LoginAttemptReadOnlyDTO;

import java.util.List;

public interface ILoginAttemptService {

    void recordAttempt(String username, String ipAddress, boolean successful);

    List<LoginAttemptReadOnlyDTO> getAllAttempts();

    List<LoginAttemptReadOnlyDTO> getAttemptsByUsername(String username);

    DashboardStatsReadOnlyDTO getDashboardStats();

    DashboardStatsReadOnlyDTO getDashboardStatsByUsername(String username);
}
