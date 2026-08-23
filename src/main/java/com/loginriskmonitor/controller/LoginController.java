package com.loginriskmonitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Principal principal) {
        if (principal == null) {
            return "login";
        }

        return "redirect:/dashboard";
    }

    @GetMapping("/")
    public String root(Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        return "redirect:/dashboard";
    }
}