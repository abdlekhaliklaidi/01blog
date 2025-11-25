package com.dev.backend.repositories;

import com.dev.backend.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByReporterId(Long reporterId);

    List<Report> findByReportedUserId(Long reportedUserId);

    List<Report> findByPostId(Long postId);
}