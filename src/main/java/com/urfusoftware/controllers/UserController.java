package com.urfusoftware.controllers;

import com.urfusoftware.domain.Role;
import com.urfusoftware.domain.User;
import com.urfusoftware.repositories.RoleRepository;
import com.urfusoftware.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/user-list")
    public String userList(Model model) {
        model.addAttribute("users", userRepository.findAll());

        return "user-list";
    }

    @GetMapping("/user-edit/{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("role", roleRepository.findAll());
        return "user-edit";
    }

    @PostMapping("/user-edit/{user}")
    public String userSave(
            @RequestParam String username,
            @RequestParam String surname,
            @RequestParam String name,
            @RequestParam String password,
            @RequestParam("userId") User user,
            @RequestParam String role, Model model
    ) {
        if (name.isEmpty() || surname.isEmpty() || username.isEmpty() || password.isEmpty()) {
            model.addAttribute("message", "ОШИБКА! Поля не могут быть пустыми!");
            return "redirect:/user-edit/{user}";
        }
        else {
            user.setUsername(username);
            user.setName(name);
            user.setSurname(surname);
            user.setPassword(password);
            user.setRole(roleRepository.findAll().get(Integer.parseInt(role) - 1));
            userRepository.save(user);
        }
        return "redirect:/user-list";
    }
}
