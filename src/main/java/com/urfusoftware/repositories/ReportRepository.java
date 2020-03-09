package com.urfusoftware.repositories;

import com.urfusoftware.domain.Report;
import org.springframework.data.repository.CrudRepository;

public interface ReportRepository extends CrudRepository<Report, Long> {
}
