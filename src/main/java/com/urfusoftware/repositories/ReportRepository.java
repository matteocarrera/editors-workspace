package com.urfusoftware.repositories;

import com.urfusoftware.domain.Project;
import com.urfusoftware.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByOrderByIdAsc();
    List<Report> findAllByAccepted(Boolean accepted);
    List<Report> findAllByProjectAndAcceptedFalse(Project project);
    List<Report> findAllByProject(Project project);
}
