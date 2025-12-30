package com.dev.backend.dto;

import com.dev.backend.entities.Post;


public class PostDetailsDTO {
    private Long id;
    private String title;
    private String content;
    private boolean hidden;
    private String authorName;

    public PostDetailsDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.hidden = post.isHidden();
        this.authorName =
            post.getAuthor().getFirstname() + " " + post.getAuthor().getLastname();
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public boolean isHidden() { return hidden; }
    public String getAuthorName() { return authorName; }
}
