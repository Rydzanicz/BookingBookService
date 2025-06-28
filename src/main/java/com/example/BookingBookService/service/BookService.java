package com.example.BookingBookService.service;

import com.example.BookingBookService.controler.request.AddBookRequest;
import com.example.BookingBookService.entity.UserBookEntity;
import com.example.BookingBookService.entity.UsersEntity;
import com.example.BookingBookService.repository.UserBookRepository;
import com.example.BookingBookService.repository.UsersEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookService {

    private final UserBookRepository userBookRepository;

    private final UsersEntityRepository usersEntityRepository;

    public BookService(final UserBookRepository userBookRepository, final UsersEntityRepository usersEntityRepository) {
        this.userBookRepository = userBookRepository;
        this.usersEntityRepository = usersEntityRepository;
    }

    public UserBookEntity addBookToCollection(final AddBookRequest addBookRequest) {
        final Optional<UsersEntity> userOpt = usersEntityRepository.findByUsername(addBookRequest.getUserName());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found: " + addBookRequest.getUserName());
        }

        final UsersEntity user = userOpt.get();

        if (userBookRepository.existsByUserAndGoogleBookId(user, addBookRequest.getGoogleBookId())) {
            throw new RuntimeException("Book already exists in user collection");
        }

        final UserBookEntity userBookEntity = new UserBookEntity(user,
                                                                 addBookRequest.getGoogleBookId(),
                                                                 addBookRequest.getTitle(),
                                                                 addBookRequest.getAuthors(),
                                                                 addBookRequest.getDescription());

        return userBookRepository.save(userBookEntity);
    }

    public List<UserBookEntity> getUserBooks(final String username) {
        final Optional<UsersEntity> user = usersEntityRepository.findByUsername(username);
        if (user.isPresent()) {
            return userBookRepository.findByUser(user.get());
        }
        throw new RuntimeException("User not found: " + username);
    }

    public void removeBookFromCollection(final String username, final String googleBookId) {
        final Optional<UsersEntity> user = usersEntityRepository.findByUsername(username);
        if (user.isPresent()) {
            userBookRepository.deleteByUserAndGoogleBookId(user.get(), googleBookId);
        } else {
            throw new RuntimeException("User not found: " + username);
        }
    }

    public Optional<UserBookEntity> getUserBook(final String username, final String googleBookId) {
        final Optional<UsersEntity> user = usersEntityRepository.findByUsername(username);
        if (user.isPresent()) {
            return userBookRepository.findByUserAndGoogleBookId(user.get(), googleBookId);
        }
        return Optional.empty();
    }

}
