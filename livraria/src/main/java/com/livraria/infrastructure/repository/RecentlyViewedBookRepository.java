package com.livraria.infrastructure.repository;

import com.livraria.domain.model.Book;
import com.livraria.domain.model.RecentlyViewedBook;
import com.livraria.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecentlyViewedBookRepository extends JpaRepository<RecentlyViewedBook, Long> {

    List<RecentlyViewedBook> findTop10ByUserOrderByViewedAtDesc(Usuario user);

    void deleteByUserAndBook(Usuario user, Book book);
}
