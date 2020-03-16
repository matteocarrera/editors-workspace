package com.urfusoftware.controllers;

import com.urfusoftware.domain.Role;
import com.urfusoftware.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RoleController {
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/roles")
    public String roles(Model model) {
        model.addAttribute("roles", roleRepository.findAll());
        return "new-role";
    }

    @PostMapping("/roles")
    public String addRole(@RequestParam String roleName, RedirectAttributes attributes) {
        Role existingRole = roleRepository.findByName(roleName);
        if (existingRole != null) {
            attributes.addFlashAttribute("error", "ОШИБКА! Роль с таким названием уже существует!");
        } else if (roleName.isEmpty()) {
            attributes.addFlashAttribute("error", "ОШИБКА! Нельзя добавить роль без названия!");
        } else {
            Role newRole = new Role(roleName, false);
            roleRepository.save(newRole);
            attributes.addFlashAttribute("success", "Роль добавлена успешно!");
            return "redirect:/roles";
        }
        return "redirect:/roles";
    }

    @GetMapping(value = {"/roles/{role}/delete"})
    public String showDeleteRole(Model model, @PathVariable Role role)
    {
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("deleteConfirmation", true);
        return "new-role";
    }

    @PostMapping(value = {"/roles/{role}/delete"})
    public String deleteRole(@PathVariable Role role, RedirectAttributes attributes)
    {
        attributes.addFlashAttribute("success", "Роль удалена успешно!");
        roleRepository.delete(role);
        return "redirect:/roles";
    }
}
