package com.example.BookingBookService.service;

import com.example.BookingBookService.entity.ReadBookEntity;
import com.example.BookingBookService.entity.UsersEntity;
import com.example.BookingBookService.repository.ReadBookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookService {
    private final ReadBookRepository readBookRepository;

    public BookService(final ReadBookRepository readBookRepository) {
        this.readBookRepository = readBookRepository;
    }

    public List<ReadBookEntity> getUserReadBooks(final UsersEntity user) {
        return readBookRepository.findByUserOrderByIdDesc(user);
    }

    public void removeFromReadList(final UsersEntity user, final Long readBookId) {
        final ReadBookEntity readBook = readBookRepository.findById(readBookId)
                .orElseThrow(() -> new IllegalArgumentException("Read book entry not found"));

        if (!readBook.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("Cannot remove book from another user's list");
        }

        readBookRepository.delete(readBook);
    }
}