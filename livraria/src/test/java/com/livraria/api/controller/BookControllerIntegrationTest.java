package com.livraria.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.livraria.application.dto.BookDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.livraria.application.service.BookService;

import java.util.List;

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
        when(bookService.findAll()).thenReturn(List.of(new BookDTO(1L, "Title", "Author", "Genre", "Desc")));

        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Title"));
    }

    @Test
    void saveBook_shouldReturnCreated() throws Exception {
        BookDTO input = new BookDTO(null, "New Book", "Author", "Genre", "Desc");
        BookDTO output = new BookDTO(1L, "New Book", "Author", "Genre", "Desc");

        when(bookService.save(input)).thenReturn(output);

        mvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }
}
