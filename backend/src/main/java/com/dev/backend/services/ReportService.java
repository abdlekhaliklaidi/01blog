package com.dev.backend.services;

import com.dev.backend.entities.Post;
import com.dev.backend.entities.Report;
import com.dev.backend.entities.User;
import com.dev.backend.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dev.backend.repositories.UserRepository;
import com.dev.backend.dto.ReportDTO;
import com.dev.backend.repositories.PostRepository;
import com.dev.backend.dto.UserReportDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    public Report reportPost(Long postId, Long reporterId, Report report) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new RuntimeException("Reporter not found"));

        report.setPost(post);
        report.setReporter(reporter);

        return reportRepository.save(report);
    }

    public Report reportUser(Long userId, Report report) {
    User user = userRepository.findById(userId)
                  .orElseThrow(() -> new RuntimeException("User not found"));
    report.setReportedUser(user);
    return reportRepository.save(report);
    }
    
    public List<ReportDTO> getPostReports() {
    return reportRepository.findAll()
            .stream()
            .filter(r -> r.getPost() != null)
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }


    public List<UserReportDTO> getUserReports() {
    return reportRepository.findAll()
        .stream()
        .filter(r -> r.getReportedUser() != null)
        .map(this::mapToUserReportDTO)
        .collect(Collectors.toList());
    }

    private ReportDTO mapToDTO(Report report) {

    String authorName = null;
    if (report.getPost() != null && report.getPost().getAuthor() != null) {
        User author = report.getPost().getAuthor();
        authorName = author.getFirstname() + " " + author.getLastname();
    }

    String reporterEmail = report.getReporter() != null
            ? report.getReporter().getEmail()
            : null;

    return new ReportDTO(
        report.getId(),
        report.getReason(),
        report.getCreatedAt(),
        report.getPost() != null ? report.getPost().getId() : null,
        report.getPost() != null ? report.getPost().getTitle() : null,
        authorName,
        reporterEmail
    );
}
    private UserReportDTO mapToUserReportDTO(Report report) {
    User u = report.getReportedUser();

    return new UserReportDTO(
        report.getId(),
        report.getReason(),
        report.getCreatedAt(),
        u.getId(),
        u.getFirstname() + " " + u.getLastname(),
        report.getReporter() != null ? report.getReporter().getEmail() : null
    );
}

    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }
}
