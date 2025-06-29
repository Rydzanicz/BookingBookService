package com.example.BookingBookService.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookTest {
    @Test
    void ShouldCreateBookWithValidArguments() {
        // Given
        final String googleBookId = "123";
        final String title = "Example Book Title";
        final List<String> authors = List.of("Author One", "Author Two");
        final String description = "This is a sample description ofof the book.";
        final String publishedDate = "2023-01-01";
        final String pdfAcsTokenLink = "http://books.google.pl/books/download/Critical_Perspectives_on_Harry_Potter-sample-pdf.acsm?id=yfOTAgAAQBAJ&format=pdf&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api";

        // When
        final Book book = new Book(googleBookId, title, authors, description, publishedDate, pdfAcsTokenLink);

        // Then
        assertEquals(googleBookId, book.getGoogleBookId());
        assertEquals(title, book.getTitle());
        assertEquals(authors, book.getAuthors());
        assertEquals(description, book.getDescription());
        assertEquals(publishedDate, book.getPublishedDate());
    }

    @Test
    void ShouldThrowExceptionWhenGoogleBookIdIsNull() {
        // Given
        final String googleBookId = null;
        final String title = "Example Book Title";
        final List<String> authors = List.of("Author One", "Author Two");
        final String description = "This is a sample description of the book.";
        final String publishedDate = "2023-01-01";
        final String pdfAcsTokenLink = "http://books.google.pl/books/download/Critical_Perspectives_on_Harry_Potter-sample-pdf.acsm?id=yfOTAgAAQBAJ&format=pdf&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api";

        // When
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Book(googleBookId, title, authors, description, publishedDate, pdfAcsTokenLink));

        // Then
        assertEquals("googleBookId cannot be null or empty", exception.getMessage());
    }

    @Test
    void ShouldThrowExceptionWhenGoogleBookIdIsEmpty() {
        // Given
        final String googleBookId = "";
        final String title = "Example Book Title";
        final List<String> authors = List.of("Author One", "Author Two");
        final String description = "This is a sample description of the book.";
        final String publishedDate = "2023-01-01";
        final String pdfAcsTokenLink = "http://books.google.pl/books/download/Critical_Perspectives_on_Harry_Potter-sample-pdf.acsm?id=yfOTAgAAQBAJ&format=pdf&output=acs4_fulfillment_token&dl_type=sample&source=gbs_api";

        // When
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Book(googleBookId, title, authors, description, publishedDate, pdfAcsTokenLink));

        // Then
        assertEquals("googleBookId cannot be null or empty", exception.getMessage());
    }
}
