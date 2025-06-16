package com.renault.bookapi.dto;

import java.time.LocalDate;

public record BookRequestDto(
        String title,
        LocalDate publicationDate,
        String type,
        String authorName
) {}
