package com.urfusoftware.services;

import com.urfusoftware.domain.Role;
import com.urfusoftware.domain.User;
import com.urfusoftware.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    public Role findById(long id) {
        return roleRepository.findById(id).orElse(null);
    }

    public List<Role> setCurrentRole(User user) {
        List<Role> roleList = new ArrayList<>();
        for (Role role : roleRepository.findAll()) {
            if (!role.getName().equals(user.getRole().getName()))
                role.setSelected("");
            else
                role.setSelected("selected");
            roleList.add(role);
        }
        return roleList;
    }
}
