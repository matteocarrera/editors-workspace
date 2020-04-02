package com.urfusoftware.controllers;

import com.urfusoftware.domain.News;
import com.urfusoftware.domain.Project;
import com.urfusoftware.domain.User;
import com.urfusoftware.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@PreAuthorize("hasAnyAuthority('Администратор', 'Менеджер')")
public class ProjectController {
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private ProjectRepository projectRepository;
    @Autowired private NewsRepository newsRepository;
    @Autowired private ReportRepository reportRepository;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping("/project")
    public String project(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("manager", userRepository.findByRole(roleRepository.findByName("Менеджер")));

        List<User> users = getUsers("Переводчик", "Старший переводчик");

        model.addAttribute("translator", users);

        users = getUsers("Редактор", "Старший редактор");

        model.addAttribute("editor", users);
        model.addAttribute("user", currentUser);
        return "create-project";
    }

    @PostMapping("/project")
    public String addProject(@AuthenticationPrincipal User currentUser,
                             @RequestParam String title,
                             @RequestParam User manager,
                             @RequestParam User translator,
                             @RequestParam User editor) throws ParseException {
        Project newProject = new Project(title, "В работе", manager, translator, editor);
        projectRepository.save(newProject);
        String newsText = "Пользователь " + currentUser.getName() + " " + currentUser.getSurname() +
                " (" + currentUser.getUsername() + ") добавил(а) новый проект \"" + title + "\" в систему";
        newsRepository.save(new News(newsText, dateFormat.parse(LocalDate.now().toString())));
        return "redirect:/";
    }

    private List<User> getUsers(String firstRole, String secondRole) {
        return Stream.concat(userRepository.findByRole(roleRepository.findByName(firstRole)).stream(),
                userRepository.findByRole(roleRepository.findByName(secondRole)).stream())
                .collect(Collectors.toList());
    }

    @GetMapping("/projects")
    public String loadProjectPage(@AuthenticationPrincipal User currentUser, Model model) {
        List<Project> projects = projectRepository.findAllByOrderByIdAsc();
        for (Project project: projects) {
            project.setOpened();
        }
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", currentUser);

        return "projects";
    }

    @PostMapping("/projects/{projectId}")
    public String acceptProject(@AuthenticationPrincipal User currentUser,
                                @PathVariable String projectId, RedirectAttributes attributes) throws ParseException {
        Project project = projectRepository.findById((long)(Integer.parseInt(projectId))).orElse(null);
        if (reportRepository.findAllByProjectAndAcceptedFalse(project).size() == 0) {
            project.setStatus("Завершен");
            projectRepository.save(project);
            String newsText = "Пользователь " + currentUser.getName() + " " + currentUser.getSurname() +
                    " (" + currentUser.getUsername() + ") закрыл(а) проект \"" + project.getTitle() + "\"";
            newsRepository.save(new News(newsText, dateFormat.parse(LocalDate.now().toString())));
        } else attributes.addFlashAttribute("error", "В проекте остались непринятые отчеты!");

        return "redirect:/projects";
    }
}
