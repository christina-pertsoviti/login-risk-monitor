package com.loginriskmonitor.controller;

import com.loginriskmonitor.dto.LoginAttemptReadOnlyDTO;
import com.loginriskmonitor.service.ILoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LoginAttemptController {

    private final ILoginAttemptService loginAttemptService;

    @GetMapping("/login-attempts")
    public String getLoginAttempts(
            @RequestParam(name = "username", required = false)
            String username,
            Model model
    ) {
        String normalizedUsername =
                username == null ? "" : username.trim();

        List<LoginAttemptReadOnlyDTO> attempts =
                normalizedUsername.isBlank()
                        ? loginAttemptService.getAllAttempts()
                        : loginAttemptService.getAttemptsByUsername(
                        normalizedUsername
                );

        model.addAttribute("attempts", attempts);
        model.addAttribute("username", normalizedUsername);

        return "login-attempts";
    }

    @GetMapping("/my-login-history")
    public String getMyLoginHistory(
            Authentication authentication,
            Model model
    ) {
        String username = authentication.getName();

        List<LoginAttemptReadOnlyDTO> attempts =
                loginAttemptService.getAttemptsByUsername(username);

        model.addAttribute("attempts", attempts);
        model.addAttribute("username", username);

        return "my-login-history";
    }
}
