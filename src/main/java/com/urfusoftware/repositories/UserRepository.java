package com.urfusoftware.repositories;

import com.urfusoftware.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
