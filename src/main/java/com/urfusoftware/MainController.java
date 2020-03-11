package com.urfusoftware;

import com.urfusoftware.domain.Role;
import com.urfusoftware.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name,
                           Model model)
    {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping()
    public String main(Model model) {
        model.addAttribute("some", "Hello again, this is our main page!");
        return "main";
    }

    @GetMapping("/new-role")
    public String newRole() {
        return "new-role";
    }

    @PostMapping("/new-role")
    public String addRole(@RequestParam String roleName) {
        List<Role> roles = roleRepository.findByName(roleName);
        if (roles.size() < 1) {
            Role newRole = new Role(roleName);

            roleRepository.save(newRole);

            return "redirect:/";
        }
        return "new-role";
    }
}