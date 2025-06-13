package com.livraria.application.service;

import com.livraria.domain.model.Book;
import com.livraria.domain.model.RecentlyViewedBook;
import com.livraria.domain.model.Usuario;
import com.livraria.infrastructure.repository.RecentlyViewedBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentlyViewedBookService {

    private final RecentlyViewedBookRepository recentlyViewedBookRepository;

    @Transactional
    public void saveView(Usuario user, Book book) {
        // Se já existe visualização, remove para atualizar a data
        recentlyViewedBookRepository.deleteByUserAndBook(user, book);

        RecentlyViewedBook rvb = RecentlyViewedBook.builder()
                .user(user)
                .book(book)
                .viewedAt(LocalDateTime.now())
                .build();

        recentlyViewedBookRepository.save(rvb);
    }

    public List<RecentlyViewedBook> getRecentlyViewedBooks(Usuario user) {
        return recentlyViewedBookRepository.findTop10ByUserOrderByViewedAtDesc(user);
    }
}
