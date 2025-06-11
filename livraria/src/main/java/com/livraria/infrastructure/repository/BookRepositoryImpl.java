package com.livraria.infrastructure.repository;

import com.livraria.domain.model.Book;
import com.livraria.domain.ports.BookRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryPort {

    private final BookJpaRepository jpaRepository;

    @Override
    public List<Book> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return jpaRepository.findByAuthorIgnoreCaseContaining(author);
    }

    @Override
    public List<Book> findByGenre(String genre) {
        return jpaRepository.findByGenreIgnoreCaseContaining(genre);
    }

    @Override
    public Book save(Book book) {
        return jpaRepository.save(book);
    }
}
