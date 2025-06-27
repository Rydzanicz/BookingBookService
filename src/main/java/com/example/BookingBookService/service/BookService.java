package com.example.BookingBookService.service;

import com.example.BookingBookService.entity.ReadBookEntity;
import com.example.BookingBookService.entity.UsersEntity;
import com.example.BookingBookService.repository.ReadBookRepository;
import com.example.BookingBookService.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookService {

    @Autowired
    private ReadBookRepository readBookRepository;

    @Autowired
    private UsersRepository usersRepository;

    public boolean isBookInUserCollection(String username, String googleBookId) {
        Optional<UsersEntity> user = usersRepository.findByUsername(username);
        if (user.isPresent()) {
            return readBookRepository.existsByUserAndGoogleBookId(user.get(), googleBookId);
        }
        return false;
    }

    public ReadBookEntity addBookToCollection(String username,
                                              String googleBookId,
                                              String title,
                                              String authors,
                                              String description,
                                              String publishedDate,
                                              Integer pageCount,
                                              String categories,
                                              String thumbnailUrl) {
        Optional<UsersEntity> userOpt = usersRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found: " + username);
        }

        UsersEntity user = userOpt.get();

        if (readBookRepository.existsByUserAndGoogleBookId(user, googleBookId)) {
            throw new RuntimeException("Book already exists in user collection");
        }

        ReadBookEntity readBook = new ReadBookEntity(user, googleBookId, title);
        readBook.setAuthors(authors);
        readBook.setDescription(description);
        readBook.setPublishedDate(publishedDate);
        readBook.setPageCount(pageCount);
        readBook.setCategories(categories);
        readBook.setThumbnailUrl(thumbnailUrl);

        return readBookRepository.save(readBook);
    }

    public List<ReadBookEntity> getUserBooks(String username) {
        Optional<UsersEntity> user = usersRepository.findByUsername(username);
        if (user.isPresent()) {
            return readBookRepository.findByUserOrderByAddedAtDesc(user.get());
        }
        throw new RuntimeException("User not found: " + username);
    }

    public void removeBookFromCollection(String username, String googleBookId) {
        Optional<UsersEntity> user = usersRepository.findByUsername(username);
        if (user.isPresent()) {
            readBookRepository.deleteByUserAndGoogleBookId(user.get(), googleBookId);
        } else {
            throw new RuntimeException("User not found: " + username);
        }
    }

    public Optional<ReadBookEntity> getUserBook(String username, String googleBookId) {
        Optional<UsersEntity> user = usersRepository.findByUsername(username);
        if (user.isPresent()) {
            return readBookRepository.findByUserAndGoogleBookId(user.get(), googleBookId);
        }
        return Optional.empty();
    }

    public long countUserBooks(String username) {
        Optional<UsersEntity> user = usersRepository.findByUsername(username);
        if (user.isPresent()) {
            return readBookRepository.countByUser(user.get());
        }
        return 0;
    }

    public List<ReadBookEntity> searchBooksByTitle(String username, String title) {
        Optional<UsersEntity> user = usersRepository.findByUsername(username);
        if (user.isPresent()) {
            return readBookRepository.findByUserAndTitleContainingIgnoreCase(user.get(), title);
        }
        throw new RuntimeException("User not found: " + username);
    }

    public List<ReadBookEntity> searchBooksByAuthor(String username, String author) {
        Optional<UsersEntity> user = usersRepository.findByUsername(username);
        if (user.isPresent()) {
            return readBookRepository.findByUserAndAuthorsContainingIgnoreCase(user.get(), author);
        }
        throw new RuntimeException("User not found: " + username);
    }
}
