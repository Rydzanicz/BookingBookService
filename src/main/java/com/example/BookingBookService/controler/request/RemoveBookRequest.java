package com.example.BookingBookService.controler.request;

import jakarta.validation.constraints.NotBlank;

public class RemoveBookRequest {
    @NotBlank
    private String googleBookId;

    @NotBlank
    private String username;

    public RemoveBookRequest() {
    }

    public RemoveBookRequest(final String googleBookId,
                             final String username) {
        this.googleBookId = googleBookId;
        this.username = username;
    }

    public String getGoogleBookId() {

        return googleBookId;
    }

    public void setGoogleBookId(String googleBookId) {
        this.googleBookId = googleBookId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
