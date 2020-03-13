package com.urfusoftware.controllers;

import com.urfusoftware.domain.Role;
import com.urfusoftware.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RoleController {
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/new-role")
    public String newRole() {
        return "new-role";
    }

    @PostMapping("/new-role")
    public String addRole(@RequestParam String roleName, Model model) {
        Role existingRole = roleRepository.findByName(roleName);
        if (existingRole != null) {
            model.addAttribute("message", "ОШИБКА! Роль с таким названием уже существует!");
        } else if (roleName.isEmpty()) {
            model.addAttribute("message", "ОШИБКА! Нельзя добавить роль без названия!");
        } else {
            Role newRole = new Role(roleName);

            roleRepository.save(newRole);

            return "redirect:/main";
        }
        return "new-role";
    }
}
