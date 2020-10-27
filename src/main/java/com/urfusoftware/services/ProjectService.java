package com.urfusoftware.services;

import com.urfusoftware.domain.Project;
import com.urfusoftware.domain.User;
import com.urfusoftware.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> findAllByStatus(String status) {
        return projectRepository.findAllByStatus(status);
    }

    public List<Project> getProjects() {
        List<Project> projects = projectRepository.findAllByOrderByIdAsc();
        for (Project project: projects) {
            project.setOpened();
        }
        return projects;
    }

    public Project findById(long id) {
        return projectRepository.findById(id).orElse(null);
    }

    public void createProject(String title, User manager, User translator, User editor) {
        Project newProject = new Project(title, "В работе", manager, translator, editor);
        projectRepository.save(newProject);
    }

    public void closeProject(Project project) {
        project.setStatus("Завершен");
        projectRepository.save(project);
    }
}
