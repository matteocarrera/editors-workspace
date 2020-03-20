package com.urfusoftware.controllers;

import com.urfusoftware.domain.Report;
import com.urfusoftware.domain.User;
import com.urfusoftware.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Controller
public class ReportController {
    @Autowired
    private ReportRepository reportRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("reports", reportRepository.findAll());

        return "create-report";
    }

    @PostMapping("/reports")
    public String reportSave(
            @AuthenticationPrincipal User user,
            @RequestParam String title,
            @RequestParam String timeSpent,
            @RequestParam String reportDate,
            @RequestParam("reportLink") MultipartFile reportFile,
            @RequestParam("resultLink") MultipartFile resultFile,
            @RequestParam String comments,
            RedirectAttributes attributes) throws ParseException, IOException {

        if (title.isEmpty() || comments.isEmpty() || reportFile.isEmpty() || resultFile.isEmpty()) {
            attributes.addFlashAttribute("message", "ОШИБКА! Поля не могут быть пустыми!");
            return "redirect:/reports";
        } else {
            Report report = new Report();

            report.setTitle(title);
            report.setReportLink(setLink(reportFile));
            report.setResultLink(setLink(resultFile));
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

    private String setLink(MultipartFile file) throws IOException {
        if (file != null) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String link = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + link));
            return link;
        }
        return "";
    }
}