package com.urfusoftware.repositories;

import com.urfusoftware.domain.Role;
import com.urfusoftware.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    List<User> findByRole(Role role);
}
