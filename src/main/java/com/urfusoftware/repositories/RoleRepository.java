package com.urfusoftware.repositories;

import com.urfusoftware.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName (String name);
}
