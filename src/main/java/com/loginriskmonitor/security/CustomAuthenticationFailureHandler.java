package com.loginriskmonitor.security;

import com.loginriskmonitor.service.ILoginAttemptService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler
        implements AuthenticationFailureHandler {

    private final ILoginAttemptService loginAttemptService;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {

        String username = request.getParameter("username");
        String ipAddress = request.getRemoteAddr();

        loginAttemptService.recordAttempt(
                username,
                ipAddress,
                false
        );

        response.sendRedirect(
                request.getContextPath() + "/login?error"
        );
    }
}
