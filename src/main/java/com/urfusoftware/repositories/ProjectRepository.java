package com.urfusoftware.repositories;

import com.urfusoftware.domain.Project;
import com.urfusoftware.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByTranslatorAndStatus(User user, String status);
    List<Project> findByEditorAndStatus(User user, String status);
    List<Project> findAllByOrderByIdAsc();
    List<Project> findAllByStatus(String status);
}
