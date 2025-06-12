package com.livraria.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.livraria.application.dto.BookDTO;
import com.livraria.application.dto.PageResponseDTO;
import com.livraria.application.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllBooks_shouldReturnOk() throws Exception {
        BookDTO book = new BookDTO(1L, "Title", "Author", "Genre", "Desc");

        PageResponseDTO<BookDTO> response = new PageResponseDTO<>(
                List.of(book), 0, 10, 1, 1, true);

        when(bookService.findAll(any(Pageable.class))).thenReturn(response);

        mvc.perform(get("/books")
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Title"))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.pageSize").value(10))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void saveBook_shouldReturnCreated() throws Exception {
        BookDTO input = new BookDTO(null, "New Book", "Author", "Genre", "Desc");
        BookDTO output = new BookDTO(1L, "New Book", "Author", "Genre", "Desc");

        when(bookService.save(any(BookDTO.class))).thenReturn(output);

        mvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Book"))
                .andExpect(jsonPath("$.author").value("Author"))
                .andExpect(jsonPath("$.genre").value("Genre"))
                .andExpect(jsonPath("$.description").value("Desc"));
    }
}
