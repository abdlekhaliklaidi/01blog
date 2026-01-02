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

    @DeleteMapping("/{id}")
    public void deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
    }
    
    @GetMapping("/posts")
    public List<ReportDTO> getPostReports(
        @RequestParam(defaultValue = "0") Long lastId,
        @RequestParam(defaultValue = "10") int limit) {
    
    if (lastId == 0) {
        return reportService.getPostReports();
    } else {
        return reportService.getPostReportsPaginated(lastId, limit);
    }
    }

    @GetMapping("/users")
    public List<UserReportDTO> getUserReports(
        @RequestParam(defaultValue = "0") Long lastId,
        @RequestParam(defaultValue = "10") int limit) {
    
    if (lastId == 0) {
        return reportService.getUserReports();
    } else {
        return reportService.getUserReportsPaginated(lastId, limit);
    }
    }

}
