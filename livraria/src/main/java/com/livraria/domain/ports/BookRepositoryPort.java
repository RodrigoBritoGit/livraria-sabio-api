package com.livraria.domain.ports;

import com.livraria.domain.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookRepositoryPort {
    Page<Book> findAll(Pageable pageable);

    Optional<Book> findById(Long id);

    Page<Book> findByAuthor(String author, Pageable pageable);

    Page<Book> findByGenre(String genre, Pageable pageable);

    Book save(Book book);
}
