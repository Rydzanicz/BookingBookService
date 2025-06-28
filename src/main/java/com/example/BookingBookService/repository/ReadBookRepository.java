package com.example.BookingBookService.repository;

import com.example.BookingBookService.entity.ReadBookEntity;
import com.example.BookingBookService.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReadBookRepository extends JpaRepository<ReadBookEntity, Long> {

    boolean existsByUserAndGoogleBookId(final UsersEntity user, final String googleBookId);

    List<ReadBookEntity> findByUserOrderByAddedAtDesc(final UsersEntity user);

    Optional<ReadBookEntity> findByUserAndGoogleBookId(final UsersEntity user, String googleBookId);

    void deleteByUserAndGoogleBookId(final UsersEntity user, final String googleBookId);
}
