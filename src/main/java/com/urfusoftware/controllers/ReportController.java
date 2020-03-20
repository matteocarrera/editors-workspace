package com.urfusoftware.controllers;

import com.urfusoftware.domain.Report;
import com.urfusoftware.domain.User;
import com.urfusoftware.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ReportController {
    @Autowired
    private ReportRepository reportRepository;


    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("reports", reportRepository.findAll());

        return "create-report";
    }

    @PostMapping("/reports")
    public String reportSave(
            @RequestParam String title,
            @RequestParam String reportDate,
            @RequestParam String timeSpent,
            @RequestParam String reportLink,
            @RequestParam String resultLink,
            @RequestParam String comments,
            @AuthenticationPrincipal User user,
            RedirectAttributes attributes) throws ParseException {
        if (title.isEmpty() || comments.isEmpty() || reportLink.isEmpty() || resultLink.isEmpty()) {
            attributes.addFlashAttribute("message", "ОШИБКА! Поля не могут быть пустыми!");
            return "redirect:/reports";
        } else {
            Report report = new Report();
            report.setTitle(title);
            report.setReportLink(reportLink);
            report.setResultLink(resultLink);
            report.setAccepted(false);
            report.setUser(user);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(reportDate);
            report.setDate(date);
            report.setTimeSpent(Integer.parseInt(timeSpent));
            report.setComments(comments);
            reportRepository.save(report);
        }
        return "redirect:/main";
    }
}