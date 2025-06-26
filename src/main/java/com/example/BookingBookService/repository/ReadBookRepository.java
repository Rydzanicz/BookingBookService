package com.example.BookingBookService.repository;

import com.example.BookingBookService.entity.ReadBookEntity;
import com.example.BookingBookService.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReadBookRepository extends JpaRepository<ReadBookEntity, Long> {

    // Zmieniona nazwa metody - używa googleBookId zamiast bookId
    boolean existsByUserAndGoogleBookId(UsersEntity user, String googleBookId);

    // Znajdź wszystkie książki użytkownika
    List<ReadBookEntity> findByUserOrderByAddedAtDesc(UsersEntity user);

    // Znajdź konkretną książkę użytkownika
    Optional<ReadBookEntity> findByUserAndGoogleBookId(UsersEntity user, String googleBookId);

    // Usuń książkę z kolekcji użytkownika
    void deleteByUserAndGoogleBookId(UsersEntity user, String googleBookId);

    // Policz książki użytkownika
    long countByUser(UsersEntity user);

    // Znajdź książki po tytule (wyszukiwanie częściowe)
    @Query("SELECT r FROM ReadBookEntity r WHERE r.user = :user AND LOWER(r.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<ReadBookEntity> findByUserAndTitleContainingIgnoreCase(@Param("user") UsersEntity user, @Param("title") String title);

    // Znajdź książki po autorze
    @Query("SELECT r FROM ReadBookEntity r WHERE r.user = :user AND LOWER(r.authors) LIKE LOWER(CONCAT('%', :author, '%'))")
    List<ReadBookEntity> findByUserAndAuthorsContainingIgnoreCase(@Param("user") UsersEntity user, @Param("author") String author);
}
