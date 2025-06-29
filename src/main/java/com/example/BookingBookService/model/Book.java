package com.example.BookingBookService.model;

import java.util.List;

public class Book {
    private String googleBookId;
    private String title;
    private List<String> authors;
    private String description;
    private String publishedDate;
    private String pdfAcsTokenLink;

    public Book(final String googleBookId,
                final String title,
                final List<String> authors,
                final String description,
                final String publishedDate,
                final String pdfAcsTokenLink) {
        if (googleBookId == null || googleBookId.isEmpty()) {
            throw new IllegalArgumentException("googleBookId cannot be null or empty");
        }
        this.googleBookId = googleBookId;
        this.title = title;
        this.authors = authors;
        this.description = description;
        this.publishedDate = publishedDate;
        this.pdfAcsTokenLink = pdfAcsTokenLink;
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

    public String getPdfAcsTokenLink() {
        return pdfAcsTokenLink;
    }
}