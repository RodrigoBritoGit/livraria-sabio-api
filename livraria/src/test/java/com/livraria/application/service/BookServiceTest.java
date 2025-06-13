package com.livraria.application.service;

import com.livraria.application.dto.BookDTO;
import com.livraria.application.dto.PageResponseDTO;
import com.livraria.domain.model.Book;
import com.livraria.domain.ports.BookRepositoryPort;
import com.livraria.infrastructure.exception.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class BookServiceTest {

    @Mock
    private BookRepositoryPort bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private BookDTO bookDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookService = new BookService(bookRepository); // precisa disso pois o ModelMapper está direto

        book = new Book();
        book.setId(1L);
        book.setTitle("Dom Casmurro");
        book.setAuthor("Machado de Assis");
        book.setGenre("Romance");

        bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Dom Casmurro");
        bookDTO.setAuthor("Machado de Assis");
        bookDTO.setGenre("Romance");

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void testFindAll() {
        Page<Book> page = new PageImpl<>(List.of(book), pageable, 1);
        when(bookRepository.findAll(pageable)).thenReturn(page);

        PageResponseDTO<BookDTO> result = bookService.findAll(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("Dom Casmurro", result.getContent().get(0).getTitle());
    }

    @Test
    void testFindById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookDTO result = bookService.findById(1L);

        assertEquals("Dom Casmurro", result.getTitle());
        verify(bookRepository).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.findById(99L));
    }

    @Test
    void testFindByAuthor() {
        Page<Book> page = new PageImpl<>(List.of(book), pageable, 1);
        when(bookRepository.findByAuthor("Machado de Assis", pageable)).thenReturn(page);

        PageResponseDTO<BookDTO> result = bookService.findByAuthor("Machado de Assis", pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("Machado de Assis", result.getContent().get(0).getAuthor());
    }

    @Test
    void testFindByGenre() {
        Page<Book> page = new PageImpl<>(List.of(book), pageable, 1);
        when(bookRepository.findByGenre("Romance", pageable)).thenReturn(page);

        PageResponseDTO<BookDTO> result = bookService.findByGenre("Romance", pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("Romance", result.getContent().get(0).getGenre());
    }

    @Test
    void testSave() {
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDTO result = bookService.save(bookDTO);

        assertEquals(bookDTO.getTitle(), result.getTitle());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void testGetEntityByIdSuccess() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.getEntityById(1L);

        assertNotNull(result);
        assertEquals("Dom Casmurro", result.getTitle());
    }

    @Test
    void testGetEntityByIdNotFound() {
        when(bookRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.getEntityById(100L));
    }

    @Test
    void testConvertToDTO() {
        BookDTO dto = bookService.convertToDTO(book);
        assertEquals(book.getTitle(), dto.getTitle());
    }

    @Test
    void testConvertToEntity() {
        Book entity = bookService.convertToEntity(bookDTO);
        assertEquals(bookDTO.getTitle(), entity.getTitle());
    }
}
