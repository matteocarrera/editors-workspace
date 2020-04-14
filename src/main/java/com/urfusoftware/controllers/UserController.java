package com.urfusoftware.controllers;

import com.urfusoftware.domain.User;
import com.urfusoftware.services.RoleService;
import com.urfusoftware.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@PreAuthorize("hasAuthority('Администратор')")
public class UserController {
    @Autowired private UserService userService;
    @Autowired private RoleService roleService;

    @GetMapping("/users")
    public String userList(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("user", currentUser);
        return "user-list";
    }

    @GetMapping("/users/{user}")
    public String userEditForm(@AuthenticationPrincipal User currentUser, @PathVariable User user, Model model) {
        if (!currentUser.getId().equals(user.getId())) {
            model.addAttribute("allowDelete", true);
        }
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("user", user);
        model.addAttribute("role", roleService.setCurrentRole(user));
        return "user-edit";
    }

    @PostMapping("/users/{user}")
    public String userSave(@RequestParam String username, @RequestParam String surname,
                           @RequestParam String name, @RequestParam("userId") User user,
                           @RequestParam String role) {
        userService.saveUser(user, username, name, surname, role);
        return "redirect:/users";
    }
}