package com.renault.bookapi.service;

import com.renault.bookapi.dto.AuthorDto;
import com.renault.bookapi.entity.Author;
import com.renault.bookapi.mapper.AuthorMapper;
import com.renault.bookapi.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AuthorServiceTest {

    @Mock
    AuthorRepository repo;
    @Spy
    AuthorMapper mapper = Mappers.getMapper(AuthorMapper.class);
    @InjectMocks
    AuthorService service;

    private Author a1, a2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        a1 = new Author(); a1.setId(1L); a1.setName("Hugo"); a1.setAge(80); a1.setFollowersNumber(1000);
        a2 = new Author(); a2.setId(2L); a2.setName("Camus"); a2.setAge(46); a2.setFollowersNumber(2000);
    }

    @Test
    void list_returnsAllDtos() {
        when(repo.findAll()).thenReturn(List.of(a1, a2));
        List<AuthorDto> dtos = service.list();

        assertEquals(2, dtos.size());
        assertEquals("Hugo", dtos.get(0).name());
        assertEquals(2L, dtos.get(1).id());
    }
}