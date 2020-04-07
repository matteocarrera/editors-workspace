package com.urfusoftware.controllers;

import com.urfusoftware.domain.News;
import com.urfusoftware.domain.Project;
import com.urfusoftware.domain.Report;
import com.urfusoftware.domain.User;
import com.urfusoftware.repositories.ProjectRepository;
import com.urfusoftware.services.NewsService;
import com.urfusoftware.services.ReportService;
import com.urfusoftware.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

@Controller
public class ReportController {
    @Autowired private UserService userService;
    @Autowired private ReportService reportService;
    @Autowired private ProjectRepository projectRepository;
    @Autowired private NewsService newsService;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping("/reports/add")
    public String reports(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("user", currentUser);
        if (currentUser.getRole().getName().toLowerCase().contains("переводчик"))
            model.addAttribute("project", projectRepository.findByTranslatorAndStatus(currentUser, "В работе"));
        else
            model.addAttribute("project", projectRepository.findByEditorAndStatus(currentUser, "В работе"));
        model.addAttribute("tomorrow", dateFormat.format(Calendar.getInstance().getTime()));
        return "create-report";
    }

    @PostMapping("/reports/add")
    public String reportSave(@AuthenticationPrincipal User user, @RequestParam String title,
                             @RequestParam Project project, @RequestParam String timeSpent,
                             @RequestParam String reportDate, @RequestParam("reportLink") MultipartFile reportFile,
                             @RequestParam("resultLink") MultipartFile resultFile, @RequestParam String comments)
            throws ParseException, IOException {
        reportService.createReport(title, project, reportDate, Integer.parseInt(timeSpent), reportFile, resultFile,
                comments, false, user);
        String newsText = "Пользователь " + user.getName() + " " + user.getSurname() + " (" + user.getUsername() + ") добавил(а) новый отчет";
        newsService.save(new News(newsText, dateFormat.parse(LocalDate.now().toString())));
        return "redirect:/";
    }

    @GetMapping("/reports")
    private String loadReportPage(@AuthenticationPrincipal User currentUser, Model model) {
        String userRole = currentUser.getRole().getName();
        model.addAttribute("currentUser", currentUser);
        if (userRole.equals("Администратор") || userRole.equals("Менеджер")) {
            model.addAttribute("checkForWatchingPermission", true);
            model.addAttribute("checkForAcceptingPermission", true);
            model.addAttribute("users", userService.getAllUsersWithFlags());
        } else if (userRole.equals("Старший переводчик") || userRole.equals("Старший редактор")) {
            model.addAttribute("checkForWatchingPermission", true);
            String juniorRole = userRole.substring(8, 9).toUpperCase() + userRole.substring(9);
            model.addAttribute("users", userService.getMergedUsers(userRole, juniorRole));
        } else {
            model.addAttribute("user", currentUser);
            List<Report> userReports = reportService.getUserReports(currentUser);
            if (userReports.size() != 0) model.addAttribute("hasReports", true);
            model.addAttribute("reports", userReports);
        }
        return "reports";
    }

    @PostMapping("/reports")
    public String getReports(@RequestParam User selectedUser, RedirectAttributes attributes) {
        attributes.addFlashAttribute("user", selectedUser);
        List<Report> userReports = reportService.getUserReports(selectedUser);
        if (userReports.size() != 0) attributes.addFlashAttribute("hasReports", true);
        attributes.addFlashAttribute("reports", userReports);

        return "redirect:/reports";
    }

    @GetMapping("/reports/{fileName}")
    public void getFile( HttpServletResponse response,
                         @PathVariable("fileName") String fileName) throws IOException {
        reportService.getPath(response, fileName);
    }

    @PostMapping("/reports/{reportId}")
    private String acceptReport(@AuthenticationPrincipal User currentUser, @PathVariable String reportId) throws ParseException {
        Report report = reportService.findById((Integer.parseInt(reportId)));
        reportService.acceptReport(report);
        User user = report.getUser();
        String newsText = "Пользователь " + currentUser.getName() + " " + currentUser.getSurname() +
                " (" + currentUser.getUsername() + ") принял(а) отчет пользователя " + user.getName() +
                " " + user.getSurname() + " (" + user.getUsername() + ")";
        newsService.save(new News(newsText, dateFormat.parse(LocalDate.now().toString())));
        return "redirect:/reports";
    }
}