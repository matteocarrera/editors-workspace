package com.urfusoftware.repositories;

import com.urfusoftware.domain.Project;
import com.urfusoftware.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByTranslator(User user);
    List<Project> findByEditor(User user);
    List<Project> findAllByOrderByIdAsc();
}
