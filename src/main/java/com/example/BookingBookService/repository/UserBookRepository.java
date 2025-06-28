package com.example.BookingBookService.repository;

import com.example.BookingBookService.entity.UserBookEntity;
import com.example.BookingBookService.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBookRepository extends JpaRepository<UserBookEntity, Long> {

    boolean existsByUserAndGoogleBookId(final UsersEntity user, final String googleBookId);

    List<UserBookEntity> findByUser(final UsersEntity user);

    Optional<UserBookEntity> findByUserAndGoogleBookId(final UsersEntity user, String googleBookId);

    void deleteByUserAndGoogleBookId(final UsersEntity user, final String googleBookId);
}
