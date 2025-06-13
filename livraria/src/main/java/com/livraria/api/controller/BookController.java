package com.livraria.api.controller;

import com.livraria.domain.model.Usuario;
import com.livraria.infrastructure.repository.UserRepository;
import com.livraria.application.service.RecentlyViewedBookService;
import com.livraria.domain.model.Book;
import com.livraria.domain.model.RecentlyViewedBook;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.livraria.application.dto.BookDTO;
import com.livraria.application.dto.PageResponseDTO;
import com.livraria.application.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final RecentlyViewedBookService recentlyViewedBookService;
    private final UserRepository userRepository;

    @Operation(summary = "Retorna livros com paginação")
    @GetMapping
    public ResponseEntity<PageResponseDTO<BookDTO>> getAllBooks(Pageable pageable) {
        return ResponseEntity.ok(bookService.findAll(pageable));
    }

    @Operation(summary = "Retorna um livro pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        BookDTO bookDTO = bookService.findById(id);

        // Obter usuário logado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            Usuario user = userRepository.findByUsername(auth.getName())
                    .orElse(null);

            if (user != null) {
                // Obter a entidade Book para salvar no histórico
                Book bookEntity = bookService.getEntityById(id);
                recentlyViewedBookService.saveView(user, bookEntity);
            }
        }

        return ResponseEntity.ok(bookDTO);
    }

    @Operation(summary = "Retorna livros visualizados recentemente")
    @GetMapping("/recently-viewed")
    public ResponseEntity<List<BookDTO>> getRecentlyViewedBooks() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return ResponseEntity.status(401).build();
        }

        Usuario user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<RecentlyViewedBook> recentlyViewed = recentlyViewedBookService.getRecentlyViewedBooks(user);

        List<BookDTO> books = recentlyViewed.stream()
                .map(rv -> bookService.convertToDTO(rv.getBook()))
                .toList();

        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Retorna livros por gênero com paginação")
    @GetMapping("/genre/{genre}")
    public ResponseEntity<PageResponseDTO<BookDTO>> getBooksByGenre(@PathVariable String genre, Pageable pageable) {
        return ResponseEntity.ok(bookService.findByGenre(genre, pageable));
    }

    @Operation(summary = "Retorna livros por autor com paginação")
    @GetMapping("/author/{author}")
    public ResponseEntity<PageResponseDTO<BookDTO>> getBooksByAuthor(@PathVariable String author, Pageable pageable) {
        return ResponseEntity.ok(bookService.findByAuthor(author, pageable));
    }

    @Operation(summary = "Salva um novo livro")
    @PostMapping
    public ResponseEntity<BookDTO> saveBook(@Valid @RequestBody BookDTO bookDTO) {
        BookDTO savedBook = bookService.save(bookDTO);
        return ResponseEntity.status(201).body(savedBook);
    }
}
