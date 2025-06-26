package com.example.BookingBookService.repository;

import com.example.BookingBookService.entity.ReadBookEntity;
import com.example.BookingBookService.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadBookRepository extends JpaRepository<ReadBookEntity, Long> {
    List<ReadBookEntity> findByUserOrderByIdDesc(final UsersEntity user);

    boolean existsByUserAndBookId(final UsersEntity user, final Long bookId);
}