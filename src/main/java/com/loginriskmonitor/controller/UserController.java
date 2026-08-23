package com.loginriskmonitor.controller;

import com.loginriskmonitor.dto.UserCreateDTO;
import com.loginriskmonitor.exception.RoleNotFoundException;
import com.loginriskmonitor.exception.UsernameAlreadyExistsException;
import com.loginriskmonitor.service.IRoleService;
import com.loginriskmonitor.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final IRoleService roleService;

    @GetMapping("/admin/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @GetMapping("/admin/users/new")
    public String showCreateUserForm(Model model) {
        if (!model.containsAttribute("userForm")) {
            model.addAttribute("userForm", new UserCreateDTO());
        }
        addRoles(model);
        return "admin/user-form";
    }

    @PostMapping("/admin/users")
    public String createUser(
            @Valid @ModelAttribute("userForm") UserCreateDTO userForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            addRoles(model);
            return "admin/user-form";
        }

        try {
            userService.createUser(userForm);
        } catch (UsernameAlreadyExistsException exception) {
            bindingResult.rejectValue(
                    "username",
                    "username.exists",
                    exception.getMessage()
            );
            addRoles(model);
            return "admin/user-form";
        } catch (RoleNotFoundException exception) {
            bindingResult.rejectValue(
                    "roleName",
                    "role.notFound",
                    exception.getMessage()
            );
            addRoles(model);
            return "admin/user-form";
        }

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "User created successfully"
        );
        return "redirect:/admin/users";
    }

    private void addRoles(Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
    }
}
