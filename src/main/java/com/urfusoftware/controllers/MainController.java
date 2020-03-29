package com.urfusoftware.controllers;

import com.urfusoftware.domain.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String main(@AuthenticationPrincipal User currentUser, Model model) {
        String userRole = currentUser.getRole().getName();
        model.addAttribute("isAuthorized", !userRole.equals("Неавторизованный"));
        model.addAttribute("isAdmin", userRole.equals("Администратор"));
        model.addAttribute("isManager", userRole.equals("Администратор") || userRole.equals("Менеджер"));
        model.addAttribute("user", currentUser);
        return "main";
    }

    @PreAuthorize("hasAuthority('Администратор')")
    @GetMapping("/roles")
    public String roles(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("user", currentUser);
        return "roles";
    }
}