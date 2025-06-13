package com.livraria.infrastructure.repository;

import com.livraria.domain.model.Book;
import com.livraria.domain.ports.BookRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryPort {

    private final BookJpaRepository jpaRepository;

    @Override
    public long count() {
        return jpaRepository.count();
    }

    @Override
    public Page<Book> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable);
    }

    @Override
    public Optional<Book> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Page<Book> findByAuthor(String author, Pageable pageable) {
        return jpaRepository.findByAuthorIgnoreCaseContaining(author, pageable);
    }

    @Override
    public Page<Book> findByGenre(String genre, Pageable pageable) {
        return jpaRepository.findByGenreIgnoreCaseContaining(genre, pageable);
    }

    @Override
    public Book save(Book book) {
        return jpaRepository.save(book);
    }
}
