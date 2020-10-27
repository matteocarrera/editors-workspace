package com.urfusoftware.services;

import com.urfusoftware.domain.Project;
import com.urfusoftware.domain.Report;
import com.urfusoftware.domain.User;
import com.urfusoftware.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Value("${upload.path}")
    private String uploadPath;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public List<Report> findAllByAccepted(boolean flag) {
        return reportRepository.findAllByAccepted(flag);
    }

    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    public Report findById(long id) {
        return reportRepository.findById(id).orElse(null);
    }

    public List<Report> findAllByProjectAndAcceptedFalse(Project project) {
        return reportRepository.findAllByProjectAndAcceptedFalse(project);
    }

    public void acceptReport(Report report) {
        report.setAccepted(true);
        reportRepository.save(report);
    }

    public void createReport(String title, Project project, String reportDate, int timeSpent, MultipartFile reportFile,
                             MultipartFile resultFile, String comments, Boolean accepted, User user)
            throws IOException, ParseException {
        if (comments.isEmpty()) comments = "-";
        Report report = new Report(title, project, dateFormat.parse(reportDate), timeSpent,
                setLink(reportFile), setLink(resultFile), comments, false, user);
        reportRepository.save(report);
    }

    public void getPath(HttpServletResponse response, @PathVariable("fileName") String fileName) throws IOException {
        Path filePath = Paths.get(uploadPath, fileName);
        if (Files.exists(filePath))
        {
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName.substring(37));
            Files.copy(filePath, response.getOutputStream());
            response.getOutputStream().flush();
        }
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

    public List<Report> getUserReports(User user) {
        List<Report> userReports = new ArrayList<>();

        for (Report report : reportRepository.findAllByOrderByIdAsc()) {
            if (report.getUser().getId().equals(user.getId())) {
                String reportDate = report.getReportDate().toString().substring(0, 10);
                String dateInRusFormat = reportDate.substring(8, 10) +
                        "." + reportDate.substring(5, 7) +
                        "." + reportDate.substring(0, 4);
                report.setStringDate(dateInRusFormat);
                userReports.add(report);
            }
        }
        return userReports;
    }

    public List<Report> findByProject(Project project) {
        return reportRepository.findAllByProject(project);
    }
}