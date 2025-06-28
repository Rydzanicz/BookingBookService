package com.example.BookingBookService.service;

import com.example.BookingBookService.model.Book;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
public class GoogleBooksService {

    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes";
    private final RestTemplate restTemplate;
    private final String apiKey;

    public GoogleBooksService(final RestTemplate restTemplate, final @Value("${google.books.api.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }


    public List<Book> searchBooks(final String query, final int maxResults) {
        final String url = UriComponentsBuilder.fromHttpUrl(BASE_URL).queryParam("q", query).queryParam("maxResults", maxResults)
                                               .queryParam("key", apiKey).toUriString();

        try {
            final String response = restTemplate.getForObject(url, String.class);

            final ObjectMapper objectMapper = new ObjectMapper();
            final Map<String, Object> jsonResponse = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});
            return BookParser.parseBooksFromJson(jsonResponse);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch or parse books data", e);
        }
    }

}
