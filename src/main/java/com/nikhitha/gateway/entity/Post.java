package com.nikhitha.gateway.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long authorId;
    private String authorType;

    @Column(length = 1000)
    private String content;

    private LocalDateTime createdAt;

    // ✅ REQUIRED METHODS

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public String getAuthorType() { return authorType; }
    public void setAuthorType(String authorType) { this.authorType = authorType; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}