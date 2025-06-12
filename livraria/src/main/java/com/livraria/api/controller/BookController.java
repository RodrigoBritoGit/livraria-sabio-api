package com.livraria.api.controller;

import com.livraria.application.dto.BookDTO;
import com.livraria.application.dto.PageResponseDTO;
import com.livraria.application.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @Operation(summary = "Retorna livros com paginação")
    @GetMapping
    public ResponseEntity<PageResponseDTO<BookDTO>> getAllBooks(Pageable pageable) {
        return ResponseEntity.ok(bookService.findAll(pageable));
    }

    @Operation(summary = "Retorna um livro pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
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
