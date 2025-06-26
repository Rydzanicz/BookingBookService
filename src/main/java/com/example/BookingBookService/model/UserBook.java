package com.example.BookingBookService.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_books",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "google_book_id"})
        })
public class UserBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "google_book_id", nullable = false)
    private String googleBookId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "authors", length = 500)
    private String authors;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "published_date")
    private String publishedDate;

    @Column(name = "page_count")
    private Integer pageCount;

    @Column(name = "categories", length = 500)
    private String categories;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @Column(name = "added_at")
    private LocalDateTime addedAt;

    public UserBook() {
        this.addedAt = LocalDateTime.now();
    }

    // Wszystkie gettery i settery
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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
