package com.example.BookingBookService.controler.request;

import jakarta.validation.constraints.NotBlank;

public class AddBookRequest {
    @NotBlank
    private String googleBookId;

    @NotBlank
    private String title;

    @NotBlank
    private String username;

    private String authors;

    private String description;

    private String pdfAcsTokenLink;

    public AddBookRequest() {
    }

    public AddBookRequest(final String googleBookId,
                          final String title,
                          final String username,
                          final String authors,
                          final String description,
                          final String pdfAcsTokenLink) {
        this.googleBookId = googleBookId;
        this.title = title;
        this.username = username;
        this.authors = authors;
        this.description = description;
        this.pdfAcsTokenLink = pdfAcsTokenLink;
    }

    public String getGoogleBookId() {
        return googleBookId;
    }

    public String getTitle() {
        return title;
    }

    public String getUsername() {
        return username;
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
