package com.urfusoftware.controllers;

import com.urfusoftware.domain.User;
import com.urfusoftware.repositories.RoleRepository;
import com.urfusoftware.repositories.UserRepository;
import com.urfusoftware.service.UserService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/users")
    public String userList(Model model) {
        model.addAttribute("users", userRepository.findAll());

        return "user-list";
    }

    @GetMapping("/users/{user}")
    public String userEditForm(@AuthenticationPrincipal User currentUser, @PathVariable User user, Model model) {
        if (!currentUser.getId().equals(user.getId())) {
            model.addAttribute("allowDelete", true);
        }
        model.addAttribute("user", user);
        model.addAttribute("role", roleRepository.findAll());
        return "user-edit";
    }

    @PostMapping("/users/{user}")
    public String userSave(
            @RequestParam String username,
            @RequestParam String surname,
            @RequestParam String name,
            @RequestParam String password,
            @RequestParam("userId") User user,
            @RequestParam String role,
            RedirectAttributes attributes)
    {
        if (name.isEmpty() || surname.isEmpty() || username.isEmpty() || password.isEmpty()) {
            attributes.addFlashAttribute("message", "ОШИБКА! Поля не могут быть пустыми!");
            return "redirect:/users/{user}";
        } else {
            user.setUsername(username);
            user.setName(name);
            user.setSurname(surname);
            user.setPassword(password);
            user.setRole(roleRepository.findAll().get(Integer.parseInt(role) - 1));
            userRepository.save(user);
        }
        return "redirect:/users";
    }

    @GetMapping(value = {"/users/{user}/delete"})
    public String showDeleteUser(Model model, @PathVariable User user)
    {
        model.addAttribute("user", user);
        model.addAttribute("role", roleRepository.findAll());
        model.addAttribute("deleteConfirmation", true);
        return "user-edit";
    }

    @PostMapping(value = {"/users/{user}/delete"})
    public String deleteUser(@PathVariable User user)
    {
        userRepository.delete(user);
        return "redirect:/users";
    }
}
