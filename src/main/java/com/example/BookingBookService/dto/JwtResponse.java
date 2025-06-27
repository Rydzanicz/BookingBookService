package com.example.BookingBookService.dto;

public class JwtResponse {
    private final String accessToken;
    private final String type = "Bearer";
    private final Long id;
    private final String username;
    private final String email;

    public JwtResponse(String accessToken, Long id, String username, String email) {
        this.accessToken = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public String getAccessToken() {return accessToken;}

    public String getTokenType() {return type;}

    public Long getId() {return id;}

    public String getUsername() {return username;}

    public String getEmail() {return email;}
}
