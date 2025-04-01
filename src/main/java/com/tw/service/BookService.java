package com.tw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tw.dto.BookDTO;
import com.tw.entity.Book;
import com.tw.repository.BookRepo;

@Service
public class BookService {
	
	@Autowired
	private BookRepo bookRepo;

	public List<Book> getAllBooks() {
		return bookRepo.findAll();
	}

	public Book getBookById(Long id) {
		Book book = bookRepo.findById(id).orElseThrow(() -> new RuntimeException("book not found"));
		
		return book;
	}

	public Book addBook(BookDTO bookDTO) {
		Book book = new Book();
		book.setAuther(bookDTO.getAuther());
		book.setAvaliable(bookDTO.isAvaliable());
		book.setIsbn(bookDTO.getIsbn());
		book.setQuantity(bookDTO.getQuantity());
		book.setTitle(bookDTO.getTitle());
		
		return bookRepo.save(book);
	}

	public void deleteBook(Long id) {

		bookRepo.deleteById(id);
		
	}

	public Book updateBook(Long id, BookDTO bookDTO) {
		Book oldBook = bookRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("book not found"));
		
		oldBook.setAuther(bookDTO.getAuther());
		oldBook.setAvaliable(bookDTO.isAvaliable());
		oldBook.setIsbn(bookDTO.getIsbn());
		oldBook.setQuantity(bookDTO.getQuantity());
		oldBook.setTitle(bookDTO.getTitle());
		
		return bookRepo.save(oldBook);
	}

}
