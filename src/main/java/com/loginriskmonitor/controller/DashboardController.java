package com.loginriskmonitor.controller;

import com.loginriskmonitor.dto.DashboardStatsReadOnlyDTO;
import com.loginriskmonitor.service.ILoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private static final String ADMIN_AUTHORITY = "ROLE_ADMIN";

    private final ILoginAttemptService loginAttemptService;

    @GetMapping("/dashboard")
    public String showDashboard(
            Authentication authentication,
            Model model
    ) {
        String username = authentication.getName();

        DashboardStatsReadOnlyDTO stats =
                isAdmin(authentication)
                        ? loginAttemptService.getDashboardStats()
                        : loginAttemptService
                        .getDashboardStatsByUsername(username);

        model.addAttribute("stats", stats);

        return "dashboard";
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication
                .getAuthorities()
                .stream()
                .anyMatch(authority ->
                        ADMIN_AUTHORITY.equals(
                                authority.getAuthority()
                        )
                );
    }
}
