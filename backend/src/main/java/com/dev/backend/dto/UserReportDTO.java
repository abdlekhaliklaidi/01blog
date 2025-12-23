package com.dev.backend.dto;

import java.time.LocalDateTime;

public class UserReportDTO {

    private Long id;
    private String reason;
    private LocalDateTime createdAt;

    private Long reportedUserId;
    private String reportedUserName;

    private String reporterEmail;

    public UserReportDTO(
            Long id,
            String reason,
            LocalDateTime createdAt,
            Long reportedUserId,
            String reportedUserName,
            String reporterEmail
    ) {
        this.id = id;
        this.reason = reason;
        this.createdAt = createdAt;
        this.reportedUserId = reportedUserId;
        this.reportedUserName = reportedUserName;
        this.reporterEmail = reporterEmail;
    }

    public Long getId() { return id; }
    public String getReason() { return reason; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Long getReportedUserId() { return reportedUserId; }
    public String getReportedUserName() { return reportedUserName; }
    public String getReporterEmail() { return reporterEmail; }
}
