package com.example.BookingBookService.service;

import com.example.BookingBookService.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookParser {

    public static List<Book> parseBooksFromJson(Map<String, Object> json) {
        final List<Book> books = new ArrayList<>();

        final List<Map<String, Object>> items = (List<Map<String, Object>>) json.get("items");

        for (Map<String, Object> item : items) {
            final Map<String, Object> volumeInfo = (Map<String, Object>) item.get("volumeInfo");
            final String googleBookId = (String) item.get("id");
            final String title = (String) volumeInfo.get("title");
            final List<String> authors = (List<String>) volumeInfo.get("authors");
            final String description = (String) volumeInfo.get("description");
            final String publishedDate = (String) volumeInfo.get("publishedDate");

            books.add(new Book(googleBookId, title, authors, description, publishedDate));
        }

        return books;
    }
}