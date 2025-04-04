package com.tw.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.tw.dto.BookDTO;
import com.tw.entity.Book;
import com.tw.globalexceptionhandler.ResourceNotFoundException;
import com.tw.repository.BookRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepo bookRepo;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuther("Test Author");
        book.setIsbn("123-456-789");
        book.setQuantity(10);
        book.setAvaliable(true);

        bookDTO = new BookDTO();
        bookDTO.setTitle("New Book");
        bookDTO.setAuther("New Author");
        bookDTO.setIsbn("987-654-321");
        bookDTO.setQuantity(5);
        bookDTO.setAvaliable(false);
    }

    @Test
    void testGetAllBooks() {
        when(bookRepo.findAll()).thenReturn(Arrays.asList(book));

        List<Book> books = bookService.getAllBooks();

        assertEquals(1, books.size());
        assertEquals("Test Book", books.get(0).getTitle());
        verify(bookRepo, times(1)).findAll();
    }

    @Test
    void testGetBookById_Found() {
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));

        Book foundBook = bookService.getBookById(1L);

        assertNotNull(foundBook);
        assertEquals("Test Book", foundBook.getTitle());
        verify(bookRepo, times(1)).findById(1L);
    }

    @Test
    void testGetBookById_NotFound() {
        when(bookRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(1L));
        verify(bookRepo, times(1)).findById(1L);
    }

    @Test
    void testAddBook() {
        when(bookRepo.save(any(Book.class))).thenReturn(book);

        Book savedBook = bookService.addBook(bookDTO);

        assertNotNull(savedBook);
        assertEquals("Test Book", savedBook.getTitle());
        verify(bookRepo, times(1)).save(any(Book.class));
    }

    @Test
    void testDeleteBook_Found() {
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
        doNothing().when(bookRepo).deleteById(1L);

        assertDoesNotThrow(() -> bookService.deleteBook(1L));
        verify(bookRepo, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBook_NotFound() {
        when(bookRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.deleteBook(1L));
        verify(bookRepo, times(1)).findById(1L);
        verify(bookRepo, never()).deleteById(anyLong());
    }

    @Test
    void testUpdateBook_Found() {
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepo.save(any(Book.class))).thenReturn(book);

        Book updatedBook = bookService.updateBook(1L, bookDTO);

        assertNotNull(updatedBook);
        assertEquals("New Book", updatedBook.getTitle());
        assertEquals("New Author", updatedBook.getAuther());
        verify(bookRepo, times(1)).save(book);
    }

    @Test
    void testUpdateBook_NotFound() {
        when(bookRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.updateBook(1L, bookDTO));
        verify(bookRepo, times(1)).findById(1L);
        verify(bookRepo, never()).save(any(Book.class));
    }
}

