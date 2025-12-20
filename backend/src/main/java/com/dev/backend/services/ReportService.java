package com.dev.backend.services;

import com.dev.backend.entities.Post;
import com.dev.backend.entities.Report;
import com.dev.backend.entities.User;
import com.dev.backend.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dev.backend.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    public Report reportPost(Long postId, Report report) {
        Post post = new Post();
        post.setId(postId);
        report.setPost(post);
        return reportRepository.save(report);
    }

    // public Report reportUser(Long userId, Report report) {
    //     User user = new User();
    //     user.setId(userId);
    //     report.setReportedUser(user);
    //     return reportRepository.save(report);
    // }

    public Report reportUser(Long userId, Report report) {
    User user = userRepository.findById(userId)
                  .orElseThrow(() -> new RuntimeException("User not found"));
    report.setReportedUser(user);
    return reportRepository.save(report);
    }

    public List<Report> getPostReports() {
        return reportRepository.findAll()
                .stream()
                .filter(r -> r.getPost() != null)
                .collect(Collectors.toList());
    }

    public List<Report> getUserReports() {
        return reportRepository.findAll()
                .stream()
                .filter(r -> r.getReportedUser() != null)
                .collect(Collectors.toList());
    }

    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }
}
