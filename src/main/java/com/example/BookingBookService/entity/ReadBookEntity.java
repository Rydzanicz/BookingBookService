package com.example.BookingBookService.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "read_books")
public class ReadBookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UsersEntity user;

    @Column(name = "google_book_id", nullable = false)
    private String googleBookId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "authors")
    private String authors;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "published_date")
    private String publishedDate;

    @Column(name = "page_count")
    private Integer pageCount;

    @Column(name = "categories")
    private String categories;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "added_at")
    private LocalDateTime addedAt;

    public ReadBookEntity() {
        this.addedAt = LocalDateTime.now();
    }

    // Konstruktor z parametrami
    public ReadBookEntity(UsersEntity user, String googleBookId, String title) {
        this();
        this.user = user;
        this.googleBookId = googleBookId;
        this.title = title;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsersEntity getUser() {
        return user;
    }

    public void setUser(UsersEntity user) {
        this.user = user;
    }

    public String getGoogleBookId() {
        return googleBookId;
    }

    public void setGoogleBookId(String googleBookId) {
        this.googleBookId = googleBookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }
}
