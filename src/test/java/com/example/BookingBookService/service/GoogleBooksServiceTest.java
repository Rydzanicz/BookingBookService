package com.example.BookingBookService.service;

import com.example.BookingBookService.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class GoogleBooksServiceTest {

    @Value("fakeGoogleApiKey")
    private final String apiKey = "fakeApiKey";
    private GoogleBooksService googleBooksService;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
        googleBooksService = new GoogleBooksService(restTemplate, apiKey);
    }

    @Test
    void searchBooksShouldMapJsonToBooksCorrectly() {

        //When
        final String jsonResponse = """
                                        {
                                            "kind": "books#volumes",
                                            "totalItems": 1000000,
                                            "items": [
                                                {
                                                    "id": "ZhQ9kYNnrVoC",
                                                    "volumeInfo": {
                                                        "title": "ASC election",
                                                        "authors": ["United States. Agricultural Stabilization and Conservation Service"],
                                                        "publishedDate": "1980",
                                                        "description": "A sample description for a book."
                                                    }
                                                },
                                                {
                                                    "id": "jOhXEQAAQBAJ",
                                                    "volumeInfo": {
                                                        "title": "ASC and AI. A Dialogue Between an ASC Master, Psychologist, and AI",
                                                        "authors": ["Sergey Kravchenko"],
                                                        "publishedDate": "2025-04-17",
                                                        "description": "Exploring the intersection of ASC and AI."
                                                    }
                                                }
                                            ]
                                        }
                                    """;

        final String query = "ASC";
        final int maxResults = 10;
        final String url = "https://www.googleapis.com/books/v1/volumes?q=ASC&maxResults=10&key=" + apiKey;

        mockServer.expect(requestTo(url)).andExpect(request -> assertEquals(HttpMethod.GET, request.getMethod()))
                  .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        // When
        final List<Book> books = googleBooksService.searchBooks(query, maxResults);

        // Then
        assertNotNull(books);
        assertEquals(2, books.size());

        Book book1 = books.get(0);

        assertEquals("ZhQ9kYNnrVoC", book1.getGoogleBookId());
        assertEquals("ASC election", book1.getTitle());
        assertEquals(1, book1.getAuthors().size());
        assertEquals("United States. Agricultural Stabilization and Conservation Service", book1.getAuthors().get(0));
        assertEquals("1980", book1.getPublishedDate());
        assertEquals("A sample description for a book.", book1.getDescription());

        Book book2 = books.get(1);
        assertEquals("jOhXEQAAQBAJ", book2.getGoogleBookId());
        assertEquals("ASC and AI. A Dialogue Between an ASC Master, Psychologist, and AI", book2.getTitle());
        assertEquals(1, book2.getAuthors().size());
        assertEquals("Sergey Kravchenko", book2.getAuthors().get(0));
        assertEquals("2025-04-17", book2.getPublishedDate());
        assertEquals("Exploring the intersection of ASC and AI.", book2.getDescription());

        mockServer.verify();
    }

    @Test
    void searchBooksShouldThrowExceptionOnHttpErrorResponse() {
        // Given
        final String url = createUrl("ErrorQuery", 10);

        mockServer.expect(requestTo(url)).andRespond(withServerError());

        // When
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            googleBooksService.searchBooks("ErrorQuery", 10);
        });

        //Then
        assertTrue(exception.getMessage().contains("Failed to fetch or parse books data"));
        mockServer.verify();
    }

    @Test
    void searchBooksShouldThrowExceptionWhenResponseIsMalformed() {
        // Given
        String malformedJson = "{invalid";

        String url = createUrl("MalformedQuery", 10);

        mockServer.expect(requestTo(url)).andRespond(withSuccess(malformedJson, MediaType.APPLICATION_JSON));

        // When
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            googleBooksService.searchBooks("MalformedQuery", 10);
        });
        //Then
        assertTrue(exception.getMessage().contains("Failed to fetch or parse books data"));
        mockServer.verify();
    }

    private String createUrl(String query, int maxResults) {
        return "https://www.googleapis.com/books/v1/volumes?q=" + query + "&maxResults=" + maxResults + "&key=" + apiKey;
    }

}