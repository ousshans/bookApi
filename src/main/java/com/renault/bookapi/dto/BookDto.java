package com.renault.bookapi.dto;

import java.time.LocalDate;

public record BookDto(Long id, String title, String type, LocalDate publicationDate, AuthorDto author) {
}
