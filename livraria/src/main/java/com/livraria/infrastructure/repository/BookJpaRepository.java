package com.livraria.infrastructure.repository;

import com.livraria.domain.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookJpaRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthorIgnoreCaseContaining(String author);

    List<Book> findByGenreIgnoreCaseContaining(String genre);
}
