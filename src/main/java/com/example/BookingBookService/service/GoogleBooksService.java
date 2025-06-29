package com.example.BookingBookService.service;

import com.example.BookingBookService.model.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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


    public Page<Book> searchBooks(final String query, final int page, final int size) {
        final int startIndex = page * size;
        final String url = String.format(
                BASE_URL + "?q=%s&startIndex=%d&maxResults=%d&key=%s",
                URLEncoder.encode(query, StandardCharsets.UTF_8), startIndex, size, apiKey
        );

        final Map<String, Object> result = restTemplate.getForObject(url, Map.class);
        final long totalItems = result.get("totalItems") != null
                ? ((Number) result.get("totalItems")).longValue()
                : 0;

        final List<Book> books = BookParser.parseBooksFromJson(result);
        final Pageable pageable = PageRequest.of(page, size);
        return new PageImpl<>(books, pageable, totalItems);
    }
}
