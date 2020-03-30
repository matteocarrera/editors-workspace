package com.urfusoftware.controllers;

import com.urfusoftware.domain.News;
import com.urfusoftware.domain.Report;
import com.urfusoftware.domain.User;
import com.urfusoftware.repositories.NewsRepository;
import com.urfusoftware.repositories.ProjectRepository;
import com.urfusoftware.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {
    @Autowired private ProjectRepository projectRepository;
    @Autowired private ReportRepository reportRepository;
    @Autowired private NewsRepository newsRepository;

    @GetMapping("/")
    public String main(@AuthenticationPrincipal User currentUser, Model model) {
        String userRole = currentUser.getRole().getName();
        model.addAttribute("user", currentUser);
        model.addAttribute("isAuthorized", !userRole.equals("Неавторизованный"));
        model.addAttribute("isAdmin", userRole.equals("Администратор"));
        model.addAttribute("isManager", userRole.equals("Администратор") || userRole.equals("Менеджер"));
        model.addAttribute("isWorker", !userRole.equals("Неавторизованный"));


        model.addAttribute("countOfDoneProjects", projectRepository.findAllByStatus("Завершен").size());
        model.addAttribute("countOfDoneReports", reportRepository.findAllByAccepted(true).size());
        model.addAttribute("countOfUndoneReports", reportRepository.findAllByAccepted(false).size());

        int sum = 0, count = 0;
        for (Report report : reportRepository.findAll()) {
            sum += report.getTimeSpent();
            count++;
        }
        model.addAttribute("averageMins", sum / count);

        model.addAttribute("news", getNewsList(currentUser));

        return "main";
    }

    @PreAuthorize("hasAuthority('Администратор')")
    @GetMapping("/roles")
    public String roles(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("user", currentUser);
        return "roles";
    }

    private List<News> getNewsList(User user) {
        String role = user.getRole().getName();
        List<News> newsList = new ArrayList<>();

        for (News news : newsRepository.findAllByOrderByIdDesc()) {
            String reportDate = news.getNewsDate().toString().substring(0, 10);
            String dateInRusFormat = reportDate.substring(8, 10) +
                    "." + reportDate.substring(5, 7) +
                    "." + reportDate.substring(0, 4);
            news.setStringDate(dateInRusFormat);
            if (role.equals("Администратор") || role.equals("Менеджер"))
                newsList.add(news);
            else if (news.getText().contains(user.getUsername()))
                newsList.add(news);
        }

        return newsList;
    }
}