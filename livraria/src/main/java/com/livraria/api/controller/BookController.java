package com.livraria.api.controller;

import com.livraria.application.dto.BookDTO;
import com.livraria.application.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "API para gerenciamento de livros")
public class BookController {

    private final BookService bookService;

    @Operation(summary = "Retorna todos os livros")
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @Operation(summary = "Retorna um livro pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @Operation(summary = "Retorna livros por gênero")
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<BookDTO>> getBooksByGenre(@PathVariable String genre) {
        return ResponseEntity.ok(bookService.findByGenre(genre));
    }

    @Operation(summary = "Retorna livros por autor")
    @GetMapping("/author/{author}")
    public ResponseEntity<List<BookDTO>> getBooksByAuthor(@PathVariable String author) {
        return ResponseEntity.ok(bookService.findByAuthor(author));
    }

    @Operation(summary = "Salva um novo livro")
    @PostMapping
    public ResponseEntity<BookDTO> saveBook(@Valid @RequestBody BookDTO bookDTO) {
        BookDTO savedBook = bookService.save(bookDTO);
        return ResponseEntity.status(201).body(savedBook);
    }
}
