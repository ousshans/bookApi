package com.renault.bookapi.controller;

import com.renault.bookapi.dto.AuthorDto;
import com.renault.bookapi.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AuthorService service;

    @Test
    void list_ok() throws Exception {
        when(service.list())
                .thenReturn(List.of(new AuthorDto(1L, "Hugo", 80, 1000)));

        mvc.perform(get("/api/authors")
                        .header("api-key", "aedz-151-ftyh-554"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Hugo"));
    }
}