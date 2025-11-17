package com.dev.backend.repositories;

import com.dev.backend.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByPostId(Long postId);
    List<Report> findByUserId(Long userId);
}
