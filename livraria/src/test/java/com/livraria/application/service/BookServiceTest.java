package com.livraria.application.service;

import com.livraria.application.dto.BookDTO;
import com.livraria.application.dto.PageResponseDTO;
import com.livraria.domain.model.Book;
import com.livraria.domain.ports.BookRepositoryPort;
import com.livraria.infrastructure.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepositoryPort repository;

    @InjectMocks
    private BookService service;

    private final ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_shouldReturnPagedDTO() {
        // Arrange
        Book book = new Book(1L, "Title", "Author", "Genre", "Desc");
        List<Book> books = List.of(book);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> page = new PageImpl<>(books, pageable, books.size());

        when(repository.findAll(pageable)).thenReturn(page);

        // Act
        PageResponseDTO<BookDTO> result = service.findAll(pageable);

        // Assert
        assertEquals(1, result.getContent().size());
        assertEquals("Title", result.getContent().get(0).getTitle());
        assertEquals(0, result.getPageNumber());
        assertEquals(10, result.getPageSize());
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertFalse(result.isLast());
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
