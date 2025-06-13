package com.livraria.api.controller;

import com.livraria.application.dto.BookDTO;
import com.livraria.application.dto.PageResponseDTO;
import com.livraria.application.service.BookService;
import com.livraria.application.service.RecentlyViewedBookService;
import com.livraria.domain.model.Book;
import com.livraria.domain.model.Usuario;
import com.livraria.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class BookControllerTest {

    @InjectMocks
    private BookController bookController;

    @Mock
    private BookService bookService;

    @Mock
    private RecentlyViewedBookService recentlyViewedBookService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void testGetAllBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        List<BookDTO> content = List.of(new BookDTO());
        PageResponseDTO<BookDTO> mockPage = new PageResponseDTO<>(
                content,
                0, // pageNumber
                10, // pageSize
                1L, // totalElements
                1, // totalPages
                true // last
        );
        when(bookService.findAll(pageable)).thenReturn(mockPage);

        ResponseEntity<PageResponseDTO<BookDTO>> response = bookController.getAllBooks(pageable);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPage, response.getBody());
    }

    @Test
    void testGetBookById_withAuthenticatedUser() {
        Long bookId = 1L;
        BookDTO mockBookDTO = new BookDTO();
        Book mockBook = new Book();
        Usuario mockUser = new Usuario();

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("john");
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(mockUser));
        when(bookService.findById(bookId)).thenReturn(mockBookDTO);
        when(bookService.getEntityById(bookId)).thenReturn(mockBook);

        ResponseEntity<BookDTO> response = bookController.getBookById(bookId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBookDTO, response.getBody());
        verify(recentlyViewedBookService).saveView(mockUser, mockBook);
    }

    @Test
    void testGetBookById_anonymousUser() {
        Long bookId = 1L;
        BookDTO mockBookDTO = new BookDTO();

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("anonymousUser");
        when(bookService.findById(bookId)).thenReturn(mockBookDTO);

        ResponseEntity<BookDTO> response = bookController.getBookById(bookId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBookDTO, response.getBody());
        verify(recentlyViewedBookService, never()).saveView(any(), any());
    }

    @Test
    void testGetBooksByGenre() {
        String genre = "Ficção";
        Pageable pageable = PageRequest.of(0, 10);
        List<BookDTO> content = List.of(new BookDTO());
        PageResponseDTO<BookDTO> page = new PageResponseDTO<>(
                content,
                0, // pageNumber
                10, // pageSize
                1L, // totalElements
                1, // totalPages
                true // last
        );

        when(bookService.findByGenre(genre, pageable)).thenReturn(page);
        ResponseEntity<PageResponseDTO<BookDTO>> response = bookController.getBooksByGenre(genre, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
    }

    @Test
    void testGetBooksByAuthor() {
        String author = "Machado de Assis";
        Pageable pageable = PageRequest.of(0, 10);
        List<BookDTO> content = List.of(new BookDTO());
        PageResponseDTO<BookDTO> page = new PageResponseDTO<>(
                content,
                0, // pageNumber
                10, // pageSize
                1L, // totalElements
                1, // totalPages
                true // last
        );

        when(bookService.findByAuthor(author, pageable)).thenReturn(page);
        ResponseEntity<PageResponseDTO<BookDTO>> response = bookController.getBooksByAuthor(author, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
    }
}
