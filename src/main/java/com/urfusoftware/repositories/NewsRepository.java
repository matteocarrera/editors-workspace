package com.urfusoftware.repositories;

import com.urfusoftware.domain.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findAllByOrderByIdDesc();
}
