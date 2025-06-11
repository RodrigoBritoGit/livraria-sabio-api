package com.livraria.domain.ports;

import com.livraria.domain.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepositoryPort {
    List<Book> findAll();
    Optional<Book> findById(Long id);
    List<Book> findByAuthor(String author);
    List<Book> findByGenre(String genre);
    Book save(Book book);
}
