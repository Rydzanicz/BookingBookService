package com.example.BookingBookService.service;

import com.example.BookingBookService.entity.ReadBookEntity;
import com.example.BookingBookService.entity.UsersEntity;
import com.example.BookingBookService.repository.ReadBookRepository;
import com.example.BookingBookService.repository.UsersEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookService {

    private final ReadBookRepository readBookRepository;

    private final UsersEntityRepository usersEntityRepository;

    public BookService(final ReadBookRepository readBookRepository, final UsersEntityRepository usersEntityRepository) {
        this.readBookRepository = readBookRepository;
        this.usersEntityRepository = usersEntityRepository;
    }

    public ReadBookEntity addBookToCollection(final String username,
                                              final String googleBookId,
                                              final String title,
                                              final String authors,
                                              final String description,
                                              final String publishedDate,
                                              final Integer pageCount,
                                              final String categories,
                                              final String thumbnailUrl) {
        Optional<UsersEntity> userOpt = usersEntityRepository.findByUsername(username);
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

    public List<ReadBookEntity> getUserBooks(final String username) {
        Optional<UsersEntity> user = usersEntityRepository.findByUsername(username);
        if (user.isPresent()) {
            return readBookRepository.findByUserOrderByAddedAtDesc(user.get());
        }
        throw new RuntimeException("User not found: " + username);
    }

    public void removeBookFromCollection(final String username, final String googleBookId) {
        Optional<UsersEntity> user = usersEntityRepository.findByUsername(username);
        if (user.isPresent()) {
            readBookRepository.deleteByUserAndGoogleBookId(user.get(), googleBookId);
        } else {
            throw new RuntimeException("User not found: " + username);
        }
    }

    public Optional<ReadBookEntity> getUserBook(final String username, final String googleBookId) {
        Optional<UsersEntity> user = usersEntityRepository.findByUsername(username);
        if (user.isPresent()) {
            return readBookRepository.findByUserAndGoogleBookId(user.get(), googleBookId);
        }
        return Optional.empty();
    }

}
