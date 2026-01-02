package com.dev.backend.repositories;

import com.dev.backend.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Pageable;


public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByReporterId(Long reporterId);

    List<Report> findByReportedUserId(Long reportedUserId);

    List<Report> findByPostId(Long postId);

    List<Report> findByPostIsNotNullAndIdLessThanOrderByIdDesc(
            Long lastId,
            Pageable pageable
    );

    List<Report> findByReportedUserIsNotNullAndIdLessThanOrderByIdDesc(
            Long lastId,
            Pageable pageable
    );

}