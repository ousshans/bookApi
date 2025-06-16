package com.renault.bookapi.mapper;

import com.renault.bookapi.dto.BookDto;
import com.renault.bookapi.entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AuthorMapper.class)
public interface BookMapper {
    BookDto toDto(Book book);
    Book toEntity(BookDto dto);
}

