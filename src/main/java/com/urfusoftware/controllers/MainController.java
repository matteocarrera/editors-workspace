package com.urfusoftware.controllers;

import com.urfusoftware.domain.Report;
import com.urfusoftware.domain.User;
import com.urfusoftware.services.NewsService;
import com.urfusoftware.services.ProjectService;
import com.urfusoftware.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @Autowired private ProjectService projectService;
    @Autowired private ReportService reportService;
    @Autowired private NewsService newsService;

    @GetMapping("/")
    public String main(@AuthenticationPrincipal User currentUser, Model model) {
        String userRole = currentUser.getRole().getName();
        model.addAttribute("user", currentUser);
        model.addAttribute("isAuthorized", !userRole.equals("Неавторизованный"));
        model.addAttribute("isAdmin", userRole.equals("Администратор"));
        model.addAttribute("isManager", userRole.equals("Администратор") || userRole.equals("Менеджер"));


        model.addAttribute("countOfDoneProjects", projectService.findAllByStatus("Завершен").size());
        model.addAttribute("countOfDoneReports", reportService.findAllByAccepted(true).size());
        model.addAttribute("countOfUndoneReports", reportService.findAllByAccepted(false).size());

        int sum = 0, count = 0;
        for (Report report : reportService.findAll()) {
            sum += report.getTimeSpent();
            count++;
        }
        if (count != 0)
            model.addAttribute("averageMins", sum / count);
        else
            model.addAttribute("averageMins", 0);

        model.addAttribute("news", newsService.getNewsList(currentUser));

        return "main";
    }

    @PreAuthorize("hasAuthority('Администратор')")
    @GetMapping("/roles")
    public String roles(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("user", currentUser);
        return "roles";
    }
}