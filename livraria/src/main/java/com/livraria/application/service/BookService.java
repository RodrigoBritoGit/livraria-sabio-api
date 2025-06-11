package com.livraria.application.service;

import com.livraria.application.dto.BookDTO;
import com.livraria.domain.model.Book;
import com.livraria.domain.ports.BookRepositoryPort;
import com.livraria.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepositoryPort bookRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public List<BookDTO> findAll() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "books", key = "#id")
    public BookDTO findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
        return modelMapper.map(book, BookDTO.class);
    }

    @Cacheable(value = "booksByAuthor", key = "#author")
    public List<BookDTO> findByAuthor(String author) {
        List<Book> books = bookRepository.findByAuthor(author);
        return books.stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "booksByGenre", key = "#genre")
    public List<BookDTO> findByGenre(String genre) {
        List<Book> books = bookRepository.findByGenre(genre);
        return books.stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList());
    }

    @CacheEvict(value = { "books", "booksByAuthor", "booksByGenre" }, allEntries = true)
    public BookDTO save(BookDTO bookDTO) {
        Book book = modelMapper.map(bookDTO, Book.class);
        Book saved = bookRepository.save(book);
        return modelMapper.map(saved, BookDTO.class);
    }
}
