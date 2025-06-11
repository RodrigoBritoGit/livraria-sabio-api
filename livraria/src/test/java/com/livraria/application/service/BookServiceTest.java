package com.livraria.application.service;

import com.livraria.application.dto.BookDTO;
import com.livraria.domain.model.Book;
import com.livraria.domain.ports.BookRepositoryPort;
import com.livraria.infrastructure.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepositoryPort repository;

    @InjectMocks
    private BookService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_shouldReturnList() {
        List<Book> books = List.of(new Book(1L, "Title", "Author", "Genre", "Desc"));
        when(repository.findAll()).thenReturn(books);

        var result = service.findAll();

        assertEquals(1, result.size());
        assertEquals("Title", result.get(0).getTitle());
    }

    @Test
    void findById_found() {
        Book book = new Book(1L, "Title", "Author", "Genre", "Desc");
        when(repository.findById(1L)).thenReturn(Optional.of(book));

        BookDTO dto = service.findById(1L);

        assertEquals("Title", dto.getTitle());
    }

    @Test
    void findById_notFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(10L));
    }

    @Test
    void save_shouldReturnSavedDTO() {
        Book book = new Book(null, "Title", "Author", "Genre", "Desc");
        Book saved = new Book(1L, "Title", "Author", "Genre", "Desc");
        when(repository.save(any())).thenReturn(saved);

        BookDTO dto = new BookDTO(null, "Title", "Author", "Genre", "Desc");
        BookDTO result = service.save(dto);

        assertNotNull(result.getId());
        assertEquals("Title", result.getTitle());
    }
}
