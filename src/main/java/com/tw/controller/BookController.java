package com.tw.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tw.dto.BookDTO;
import com.tw.entity.Book;
import com.tw.service.BookService;

@RestController
@RequestMapping("/books")
public class BookController {

	private static final Logger logger = LoggerFactory.getLogger(BookController.class);
	
	@Autowired
	private BookService bookService;
	
	@GetMapping("/getAllBooks")
	public ResponseEntity<List<Book>> getAllRecords() {
        logger.info("Fetching all book records.");
        List<Book> books = bookService.getAllBooks();
        logger.info("Fetched {} books.", books.size());
        return ResponseEntity.ok(books);
    }
	
	@GetMapping("/getById-{id}")
	public ResponseEntity<?> getBook(@PathVariable Long id) {
        logger.info("Fetching book with ID: {}", id);
        Book book = bookService.getBookById(id);
        try  {
            logger.info("Book found: {}", book);
            return ResponseEntity.ok(book);
        } 
        catch(Exception e){
            logger.warn("No book found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
	
	@PostMapping("/addBook")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> saveBook(@RequestBody BookDTO bookDTO) {
		
		logger.info("Received request to save book with details: {}", bookDTO);
		bookService.addBook(bookDTO);
        logger.info("Book saved successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body("Book saved successfully.");
		
	}
	
	@PutMapping("/updateBook-{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
        logger.info("Updating book with details: {}", bookDTO);
        Book updatedBook = bookService.updateBook(id, bookDTO);
        try {
            logger.info("Book updated successfully: {}", updatedBook);
            return ResponseEntity.ok(updatedBook);
        } 
        catch(Exception e) {
            logger.warn("Failed to update book: {}", bookDTO);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
	
	@DeleteMapping("/deleteById-{id}")
	@PreAuthorize("hasRole('ADMIN')")
	 public ResponseEntity<String> deleteById(@PathVariable Long id) {
       logger.info("Deleting book with ID: {}", id);
       bookService.deleteBook(id);
       logger.info("Book with ID: {} deleted successfully.", id);
       return ResponseEntity.ok("Book deleted successfully.");
   }

}
