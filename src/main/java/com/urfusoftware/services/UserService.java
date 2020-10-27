package com.urfusoftware.services;

import com.urfusoftware.domain.Report;
import com.urfusoftware.domain.User;
import com.urfusoftware.repositories.RoleRepository;
import com.urfusoftware.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReportService reportService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByUsername(s);
    }

    public boolean addUser(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb != null) {
            return false;
        }

        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(roleService.findByName("Неавторизованный"));
        userRepository.save(user);

        return true;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void saveUser(User user, String username, String name, String surname, String role) {
        user.setUsername(username);
        user.setName(name);
        user.setSurname(surname);
        user.setRole(roleService.findById((Integer.parseInt(role))));
        userRepository.save(user);
    }

    public List<User> findByRole(String role) {
        return userRepository.findByRole(roleService.findByName(role));
    }

    public List<User> getMergedUsers(String firstRole, String secondRole) {
        return Stream.concat(userRepository.findByRole(roleService.findByName(firstRole)).stream(),
                userRepository.findByRole(roleService.findByName(secondRole)).stream())
                .collect(Collectors.toList());
    }

    public List<User> getAllUsersWithFlags() {
        List<User> updatedUsers = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            boolean flag = false;
            for (Report report : reportService.findAllByAccepted(false)) {
                if (report.getUser().equals(user)) flag = true;
            }
            user.setNotChecked(flag);
            updatedUsers.add(user);
        }
        return updatedUsers;
    }
}