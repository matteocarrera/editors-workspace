package com.urfusoftware.repositories;

import com.urfusoftware.domain.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoleRepository extends CrudRepository<Role, Long> {

    List<Role> findByName (String name);
}
