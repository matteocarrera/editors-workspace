package com.urfusoftware.controllers;

import com.urfusoftware.domain.Project;
import com.urfusoftware.domain.Report;
import com.urfusoftware.domain.User;
import com.urfusoftware.repositories.ProjectRepository;
import com.urfusoftware.repositories.ReportRepository;
import com.urfusoftware.repositories.RoleRepository;
import com.urfusoftware.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class ReportController {
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Value("${upload.path}")
    private String uploadPath;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping("/reports/add")
    public String reports(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("user", currentUser);
        if (currentUser.getRole().getName().toLowerCase().contains("переводчик"))
            model.addAttribute("project", projectRepository.findByTranslator(currentUser));
        else
            model.addAttribute("project", projectRepository.findByEditor(currentUser));
        model.addAttribute("tomorrow", dateFormat.format(Calendar.getInstance().getTime()));
        return "create-report";
    }

    @PostMapping("/reports/add")
    public String reportSave(@AuthenticationPrincipal User user, @RequestParam String title,
                             @RequestParam Project project, @RequestParam String timeSpent,
                             @RequestParam String reportDate, @RequestParam("reportLink") MultipartFile reportFile,
                             @RequestParam("resultLink") MultipartFile resultFile, @RequestParam String comments)
            throws ParseException, IOException {
        if (comments.isEmpty()) comments = "-";
        Report report = new Report(title, project, dateFormat.parse(reportDate), Integer.parseInt(timeSpent),
                setLink(reportFile), setLink(resultFile), comments, false, user);
        reportRepository.save(report);
        return "redirect:/";
    }

    @GetMapping("/reports")
    private String loadReportPage(@AuthenticationPrincipal User currentUser, Model model) {
        String userRole = currentUser.getRole().getName();
        model.addAttribute("currentUser", currentUser);
        if (userRole.equals("Администратор") || userRole.equals("Менеджер")) {
            model.addAttribute("checkForWatchingPermission", true);
            model.addAttribute("checkForAcceptingPermission", true);
            model.addAttribute("users", userRepository.findAll());
        } else if (userRole.equals("Старший переводчик") || userRole.equals("Старший редактор")) {
            model.addAttribute("checkForWatchingPermission", true);
            String juniorRole = userRole.substring(8, 9).toUpperCase() + userRole.substring(9);
            model.addAttribute("users", getUsers(userRole, juniorRole));
        } else {
            model.addAttribute("user", currentUser);
            List<Report> userReports = getUserReports(currentUser);
            if (userReports.size() != 0) model.addAttribute("hasReports", true);
            model.addAttribute("reports", userReports);
        }
        return "reports";
    }

    @PostMapping("/reports")
    public String getReports(@RequestParam User selectedUser, RedirectAttributes attributes) {
        attributes.addFlashAttribute("user", selectedUser);
        List<Report> userReports = getUserReports(selectedUser);
        if (userReports.size() != 0) attributes.addFlashAttribute("hasReports", true);
        attributes.addFlashAttribute("reports", userReports);

        return "redirect:/reports";
    }

    @GetMapping("/reports/{fileName}")
    public void getFile( HttpServletResponse response,
                         @PathVariable("fileName") String fileName) throws IOException {
        Path filePath = Paths.get(uploadPath, fileName);
        if (Files.exists(filePath))
        {
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName.substring(37));
            Files.copy(filePath, response.getOutputStream());
            response.getOutputStream().flush();
        }
    }

    @PostMapping("/reports/{reportId}")
    private String acceptReport(@PathVariable String reportId) {
        Report report = reportRepository.findById((long)(Integer.parseInt(reportId))).orElse(null);
        report.setAccepted(true);
        reportRepository.save(report);
        return "redirect:/reports";
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

    private List<Report> getUserReports(User user) {
        List<Report> userReports = new ArrayList<>();

        for (Report report : reportRepository.findAllByOrderByIdAsc()) {
            if (report.getUser().getId().equals(user.getId())) {
                String reportDate = report.getDate().toString().substring(0, 10);
                String dateInRusFormat = reportDate.substring(8, 10) +
                        "." + reportDate.substring(5, 7) +
                        "." + reportDate.substring(0, 4);
                report.setStringDate(dateInRusFormat);
                userReports.add(report);
            }
        }
        return userReports;
    }

    private List<User> getUsers(String firstRole, String secondRole) {
        return Stream.concat(userRepository.findByRole(roleRepository.findByName(firstRole)).stream(),
                userRepository.findByRole(roleRepository.findByName(secondRole)).stream())
                .collect(Collectors.toList());
    }
}