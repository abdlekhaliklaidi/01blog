package com.dev.backend.services;

import com.dev.backend.entities.Report;
import com.dev.backend.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public Report create(Report report) {
        return reportRepository.save(report);
    }

    public List<Report> getReportsForPost(Long postId) {
        return reportRepository.findByPostId(postId);
    }

    public List<Report> getReportsByUser(Long userId) {
        return reportRepository.findByUserId(userId);
    }
}
