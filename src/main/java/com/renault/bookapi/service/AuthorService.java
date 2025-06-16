package com.renault.bookapi.service;

import com.renault.bookapi.dto.AuthorDto;
import com.renault.bookapi.mapper.AuthorMapper;
import com.renault.bookapi.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository repo;
    private final AuthorMapper mapper;

    public AuthorService(AuthorRepository repo, AuthorMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public List<AuthorDto> list() {
        return repo.findAll().stream()
                .peek(System.out::println)
                .map(mapper::toDto)
                .toList();
    }
}