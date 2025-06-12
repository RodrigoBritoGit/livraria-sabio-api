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
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
        return modelMapper.map(book, BookDTO.class);
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
        Book book = modelMapper.map(bookDTO, Book.class);
        Book saved = bookRepository.save(book);
        return modelMapper.map(saved, BookDTO.class);
    }

    private PageResponseDTO<BookDTO> mapToPageResponse(Page<Book> page) {
        return new PageResponseDTO<>(
                page.getContent().stream().map(b -> modelMapper.map(b, BookDTO.class)).collect(Collectors.toList()),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast());
    }
}
