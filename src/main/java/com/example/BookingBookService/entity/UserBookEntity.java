package com.example.BookingBookService.entity;

import jakarta.persistence.*;

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

    @Column(name = "pdfAcsTokenLink", length = 2000)
    private String pdfAcsTokenLink;


    public UserBookEntity() {
    }

    public UserBookEntity(final UsersEntity user,
                          final String googleBookId,
                          final String title,
                          final String authors,
                          final String description,
                          final String pdfAcsTokenLink) {
        this.user = user;
        this.googleBookId = googleBookId;
        this.title = title;
        this.authors = authors;
        this.description = description;
        this.pdfAcsTokenLink = pdfAcsTokenLink;
    }

    public UsersEntity getUser() {
        return user;
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

    public String getPdfAcsTokenLink() {
        return pdfAcsTokenLink;
    }
}
