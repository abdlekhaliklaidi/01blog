package com.dev.backend.controllers;

import com.dev.backend.entities.Report;
import com.dev.backend.services.ReportService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.dev.backend.dto.ReportDTO;
import org.springframework.http.ResponseEntity;
import com.dev.backend.dto.UserReportDTO;

import java.util.List;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "http://localhost:4200")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/post/{postId}/reporter/{reporterId}")
    public ResponseEntity<Void> reportPost(
        @PathVariable Long postId,
        @PathVariable Long reporterId,
        @RequestBody Report report) {

    reportService.reportPost(postId, reporterId, report);
    return ResponseEntity.ok().build();
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<Void> reportUser(
        @PathVariable Long userId,
        @RequestBody Report report) {

    reportService.reportUser(userId, report);
    return ResponseEntity.ok().build();
    }
    
    @GetMapping("/posts")
    public List<ReportDTO> getPostReports() {
    return reportService.getPostReports();
    }

    @GetMapping("/users")
    public List<UserReportDTO> getUserReports() {
    return reportService.getUserReports();
    }

    @DeleteMapping("/{id}")
    public void deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
    }
}
