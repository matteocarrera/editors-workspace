package com.urfusoftware.controllers;

import com.urfusoftware.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String main(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("isAuthorized", currentUser.getRole().getId() != 1);
        model.addAttribute("isAdmin", currentUser.getRole().getId() == 2);
        model.addAttribute("user", currentUser);
        return "main";
    }

    @GetMapping("/roles")
    public String roles(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("user", currentUser);
        return "roles";
    }
}