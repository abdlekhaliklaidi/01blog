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

    @PostMapping
    public Report reportPost(@RequestBody Report report) {
        return reportService.create(report);
    }

    @GetMapping("/post/{postId}")
    public List<Report> getReportsForPost(@PathVariable Long postId) {
        return reportService.getReportsForPost(postId);
    }

    @GetMapping("/user/{userId}")
    public List<Report> getReportsByUser(@PathVariable Long userId) {
        return reportService.getReportsByUser(userId);
    }
}
