package com.example.BookingBookService.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "read_books")
public class UserBookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "google_book_id", nullable = false)
    private String googleBookId;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UsersEntity user;

    @Column(name = "authors")
    private String authors;

    @Column(name = "description", length = 2000)
    private String description;


    public UserBookEntity() {
    }

    public UserBookEntity(final UsersEntity user, final String googleBookId, final String title) {
        this.user = user;
        this.googleBookId = googleBookId;
        this.title = title;
    }

    public UsersEntity getUser() {
        return user;
    }

    public void setUser(final UsersEntity user) {
        this.user = user;
    }

    public void setAuthors(final String authors) {
        this.authors = authors;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getGoogleBookId() {
        return googleBookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public String getDescription() {
        return description;
    }
}
