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
        this.googleBookId = googleBookId;
        this.title = title;
        this.authors = authors;
        this.description = description;
        this.publishedDate = publishedDate;
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

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
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

}