package com.tw.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tw.entity.Book;

public interface BookRepo extends JpaRepository<Book, Long> {

}
