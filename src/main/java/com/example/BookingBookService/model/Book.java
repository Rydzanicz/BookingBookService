package com.example.BookingBookService.model;

import java.util.List;

public class Book {

    private String googleBookId;
    private String title;
    private List<String> authors;
    private String description;
    private String publishedDate;

    public Book() {
    }

    public Book(final String googleBookId,
                final String title,
                final List<String> authors,
                final String description,
                final String publishedDate) {
        if (googleBookId == null || googleBookId.isEmpty()) {
            throw new IllegalArgumentException("googleBookId cannot be null or empty");
        }
        this.googleBookId = googleBookId;
        this.title = title;
        this.authors = authors;
        this.description = description;
        this.publishedDate = publishedDate;
    }

    public String getGoogleBookId() {
        return googleBookId;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getDescription() {
        return description;
    }

    public String getPublishedDate() {
        return publishedDate;
    }
}