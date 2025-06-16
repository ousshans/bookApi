package com.renault.bookapi.mapper;

import com.renault.bookapi.dto.AuthorDto;
import com.renault.bookapi.entity.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorDto toDto(Author author);
    Author toEntity(AuthorDto dto);
}