package com.livraria.application.service;

import com.livraria.application.dto.BookDTO;
import com.livraria.application.dto.PageResponseDTO;
import com.livraria.domain.model.Book;
import com.livraria.domain.ports.BookRepositoryPort;
import com.livraria.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepositoryPort bookRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public PageResponseDTO<BookDTO> findAll(Pageable pageable) {
        Page<Book> page = bookRepository.findAll(pageable);
        return mapToPageResponse(page);
    }

    @Cacheable(value = "books", key = "#id")
    public BookDTO findById(Long id) {
        Book book = getEntityById(id);
        return convertToDTO(book);
    }

    @Cacheable(value = "booksByAuthor", key = "#author + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public PageResponseDTO<BookDTO> findByAuthor(String author, Pageable pageable) {
        Page<Book> page = bookRepository.findByAuthor(author, pageable);
        return mapToPageResponse(page);
    }

    @Cacheable(value = "booksByGenre", key = "#genre + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public PageResponseDTO<BookDTO> findByGenre(String genre, Pageable pageable) {
        Page<Book> page = bookRepository.findByGenre(genre, pageable);
        return mapToPageResponse(page);
    }

    @CacheEvict(value = { "books", "booksByAuthor", "booksByGenre" }, allEntries = true)
    public BookDTO save(BookDTO bookDTO) {
        Book book = convertToEntity(bookDTO);
        Book saved = bookRepository.save(book);
        return convertToDTO(saved);
    }

    // Método para buscar a entidade por id, lançando exceção se não existir
    public Book getEntityById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
    }

    // Centraliza conversão da entidade para DTO
    public BookDTO convertToDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

    // Centraliza conversão do DTO para entidade
    public Book convertToEntity(BookDTO bookDTO) {
        return modelMapper.map(bookDTO, Book.class);
    }

    // Converte a página de entidades para resposta paginada de DTOs
    private PageResponseDTO<BookDTO> mapToPageResponse(Page<Book> page) {
        return new PageResponseDTO<>(
                page.getContent().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList()),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast());
    }
}
