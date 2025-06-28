package com.example.BookingBookService.controler.request;

import jakarta.validation.constraints.NotBlank;

public class AddBookRequest {
    @NotBlank
    private String googleBookId;

    @NotBlank
    private String title;

    @NotBlank

    private String userName;

    @NotBlank
    private String authors;

    @NotBlank
    private String description;

    public AddBookRequest() {}

    public AddBookRequest(final String googleBookId,
                          final String title,
                          final String userName,
                          final String authors,
                          final String description) {
        this.googleBookId = googleBookId;
        this.title = title;
        this.userName = userName;
        this.authors = authors;
        this.description = description;
    }

    public String getGoogleBookId() {
        return googleBookId;
    }

    public String getTitle() {
        return title;
    }

    public String getUserName() {
        return userName;
    }

    public String getAuthors() {
        return authors;
    }

    public String getDescription() {
        return description;
    }

}
