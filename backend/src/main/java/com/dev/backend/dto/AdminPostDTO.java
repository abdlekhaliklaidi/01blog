package com.dev.backend.dto;

public class AdminPostDTO {

    private Long id;
    private String title;
    private String authorName;
    private boolean hidden;

    public AdminPostDTO(Long id, String title, String authorName, boolean hidden) {
        this.id = id;
        this.title = title;
        this.authorName = authorName;
        this.hidden = hidden;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthorName() { return authorName; }
    public boolean isHidden() { return hidden; }
}
