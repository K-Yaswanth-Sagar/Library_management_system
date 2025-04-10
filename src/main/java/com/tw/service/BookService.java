package com.tw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tw.dto.BookDTO;
import com.tw.entity.Book;
import com.tw.globalexceptionhandler.ResourceNotFoundException;
import com.tw.repository.BookRepo;

@Service
public class BookService {
	
	@Autowired
	private BookRepo bookRepo;

	public List<Book> getAllBooks() {
		return bookRepo.findAll();
	}

	public Book getBookById(Long id) {
		return bookRepo.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
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

		bookRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
		
		bookRepo.deleteById(id);
		
	}

	public Book updateBook(Long id, BookDTO bookDTO) {
		Book oldBook = bookRepo.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
		
		oldBook.setAuther(bookDTO.getAuther());
		oldBook.setAvaliable(bookDTO.isAvaliable());
		oldBook.setIsbn(bookDTO.getIsbn());
		oldBook.setQuantity(bookDTO.getQuantity());
		oldBook.setTitle(bookDTO.getTitle());
		
		return bookRepo.save(oldBook);
	}

}
