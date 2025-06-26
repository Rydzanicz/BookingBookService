package com.example.BookingBookService.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GoogleBooksService {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes";

    public GoogleBooksService(
            RestTemplate restTemplate,
            @Value("${google.books.api.key}") String apiKey
    ) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    public String searchBooks(String query, int maxResults) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("q", query)
                .queryParam("maxResults", maxResults)
                .queryParam("key", apiKey)
                .toUriString();

        return restTemplate.getForObject(url, String.class);
    }
}
