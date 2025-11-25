package com.dev.backend.controllers;

import com.dev.backend.entities.Report;
import com.dev.backend.services.ReportService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "http://localhost:4200")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/post/{postId}")
    public Report reportPost(@PathVariable Long postId, @RequestBody Report report) {
        return reportService.reportPost(postId, report);
    }

    @PostMapping("/user/{userId}")
    public Report reportUser(@PathVariable Long userId, @RequestBody Report report) {
        return reportService.reportUser(userId, report);
    }

    @GetMapping("/posts")
    public List<Report> getPostReports() {
        return reportService.getPostReports();
    }

    @GetMapping("/users")
    public List<Report> getUserReports() {
        return reportService.getUserReports();
    }

    @DeleteMapping("/{id}")
    public void deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
    }
}
