package com.urfusoftware.controllers;

import com.urfusoftware.domain.News;
import com.urfusoftware.domain.Project;
import com.urfusoftware.domain.User;
import com.urfusoftware.services.NewsService;
import com.urfusoftware.services.ProjectService;
import com.urfusoftware.services.ReportService;
import com.urfusoftware.services.UserService;
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

@Controller
@PreAuthorize("hasAnyAuthority('Администратор', 'Менеджер')")
public class ProjectController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private ReportService reportService;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping("/project")
    public String project(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("manager", userService.findByRole("Менеджер"));

        List<User> users = userService.getMergedUsers("Переводчик", "Старший переводчик");

        model.addAttribute("translator", users);

        users = userService.getMergedUsers("Редактор", "Старший редактор");

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
        projectService.createProject(title, manager, translator, editor);
        String newsText = "Пользователь " + currentUser.getName() + " " + currentUser.getSurname() +
                " (" + currentUser.getUsername() + ") добавил(а) новый проект \"" + title + "\" в систему";
        newsService.save(new News(newsText, dateFormat.parse(LocalDate.now().toString())));
        return "redirect:/";
    }

    @GetMapping("/projects")
    public String loadProjectPage(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("projects", projectService.getProjects());
        model.addAttribute("currentUser", currentUser);

        return "projects";
    }

    @PostMapping("/projects/{projectId}")
    public String acceptProject(@AuthenticationPrincipal User currentUser,
                                @PathVariable String projectId,
                                RedirectAttributes attributes) throws ParseException {
        Project project = projectService.findById(Integer.parseInt(projectId));
        if (reportService.findByProject(project).size() == 0)
            attributes.addFlashAttribute("error", "В проекте нет ни одного отчета!");
        else if (reportService.findAllByProjectAndAcceptedFalse(project).size() == 0) {
            projectService.closeProject(project);
            String newsText = "Пользователь " +
                    currentUser.getName() +
                    " " +
                    currentUser.getSurname() +
                    " (" +
                    currentUser.getUsername() +
                    ") закрыл(а) проект \"" +
                    project.getTitle() +
                    "\"";
            newsService.save(new News(newsText, dateFormat.parse(LocalDate.now().toString())));
        } else attributes.addFlashAttribute("error", "В проекте остались непринятые отчеты!");

        return "redirect:/projects";
    }
}
