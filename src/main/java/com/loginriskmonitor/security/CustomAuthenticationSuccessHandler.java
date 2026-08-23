package com.loginriskmonitor.security;

import com.loginriskmonitor.service.ILoginAttemptService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    private final ILoginAttemptService loginAttemptService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        String username = authentication.getName();
        String ipAddress = request.getRemoteAddr();

        loginAttemptService.recordAttempt(
                username,
                ipAddress,
                true
        );

        response.sendRedirect(
                request.getContextPath() + "/dashboard"
        );
    }
}
