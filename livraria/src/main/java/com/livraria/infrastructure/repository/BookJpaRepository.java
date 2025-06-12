package com.livraria.infrastructure.repository;

import com.livraria.domain.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookJpaRepository extends JpaRepository<Book, Long> {
    Page<Book> findByAuthorIgnoreCaseContaining(String author, Pageable pageable);

    Page<Book> findByGenreIgnoreCaseContaining(String genre, Pageable pageable);
}
