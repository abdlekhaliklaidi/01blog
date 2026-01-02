package com.dev.backend.dto;

import java.time.LocalDateTime;

public class ReportDTO {

    private Long id;
    private String reason;
    private LocalDateTime createdAt;

    private Long postId;
    private String postTitle;

    private String authorName;
    private String reporterEmail;
    private boolean hidden;

    public ReportDTO(Long id, String reason, LocalDateTime createdAt,
                     Long postId, String postTitle,
                     String authorName, String reporterEmail, boolean hidden) {
        this.id = id;
        this.reason = reason;
        this.createdAt = createdAt;
        this.postId = postId;
        this.postTitle = postTitle;
        this.authorName = authorName;
        this.reporterEmail = reporterEmail;
         this.hidden = hidden;
    }

    public Long getId() { return id; }
    public String getReason() { return reason; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Long getPostId() { return postId; }
    public String getPostTitle() { return postTitle; }
    public String getAuthorName() { return authorName; }
    public String getReporterEmail() { return reporterEmail; }
    public boolean isHidden() { return hidden; }
}
