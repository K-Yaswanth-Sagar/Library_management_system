package com.tw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.dto.BookDTO;
import com.tw.entity.Book;
import com.tw.jwt.JwtService;
import com.tw.repository.UserRepo;
import com.tw.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ImportAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private JwtService jwtService;
    
    @MockBean
    private UserRepo repo;

    
    @Autowired
    private ObjectMapper objectMapper;

    private Book sampleBook;
    private BookDTO sampleBookDTO;

    @BeforeEach
    void setUp() {
        sampleBook = new Book();
        sampleBook.setId(1L);
        sampleBook.setTitle("Sample Book");
        sampleBook.setAuther("author 1");

        sampleBookDTO = new BookDTO();
        sampleBookDTO.setTitle("Updated Book");
        sampleBookDTO.setAuther("author 2");
    }

    @Test
    void testGetAllBooks() throws Exception {
        List<Book> books = Arrays.asList(sampleBook);
        Mockito.when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/books/getAllBooks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Sample Book"));
    }

    @Test
    void testGetBookById() throws Exception {
        Mockito.when(bookService.getBookById(1L)).thenReturn(sampleBook);

        mockMvc.perform(get("/books/getById-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Sample Book"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddBook() throws Exception {
    	 Mockito.when(bookService.addBook(any(BookDTO.class))).thenReturn(sampleBook);

        mockMvc.perform(post("/books/addBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleBookDTO)))
                .andExpect(status().isCreated())  
                .andExpect(content().string("Book saved successfully.")); 
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateBook() throws Exception {
        Mockito.when(bookService.updateBook(anyLong(), any(BookDTO.class))).thenReturn(sampleBook);

        mockMvc.perform(put("/books/updateBook-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleBookDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Sample Book"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteBook() throws Exception {
        Mockito.doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/books/deleteById-1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Book deleted successfully."));
    }

    
}
