package com.urfusoftware.controllers;

import com.urfusoftware.domain.User;
import com.urfusoftware.repositories.RoleRepository;
import com.urfusoftware.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        User userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb != null) {
            model.addAttribute("message", "User exists!");
            return "registration";
        }

        user.setActive(true);
        user.setRole(roleRepository.findByName("Неавторизованный"));
        userRepository.save(user);

        return "redirect:/login";
    }
}
