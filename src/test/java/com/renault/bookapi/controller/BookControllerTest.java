package com.renault.bookapi.controller;

import com.renault.bookapi.dto.AuthorDto;
import com.renault.bookapi.dto.BookDto;
import com.renault.bookapi.dto.BookRequestDto;
import com.renault.bookapi.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BookService service;

    @Test
    void getById_ok() throws Exception {
        BookDto dto = new BookDto(1L, "Les Misérables","Roman" ,LocalDate.of(1862,1,1),
                new AuthorDto(1L,"Victor Hugo",80,120000));
        when(service.getById(1L)).thenReturn(dto);

        mvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Les Misérables"));
    }

    @Test
    void create_ok() throws Exception {
        BookDto res = new BookDto(1L, "Les Misérables","Roman" ,LocalDate.of(1862,1,1),
                new AuthorDto(2L,"Victor Hugo",80,120000));
        when(service.create(new BookRequestDto("Les Misérables", LocalDate.now(), "Roman", "Victor Hugo")))
                .thenReturn(res);

        mvc.perform(post("/api/books")
                        .contentType("application/json")
                        .content("""
                    {
                      "title":"Les Misérables",
                      "publicationDate":"%s",
                      "type":"Roman",
                      "authorName":"Victor Hugo"
                    }
                    """.formatted(LocalDate.now())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void delete_noContent() throws Exception {
        mvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }
}