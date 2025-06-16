package com.renault.bookapi.controller;

import com.renault.bookapi.dto.BookDto;
import com.renault.bookapi.dto.BookRequestDto;
import com.renault.bookapi.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookDto> create(@RequestBody BookRequestDto dto) {
        return ResponseEntity.ok(bookService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> update(@PathVariable Long id,
                                          @RequestBody BookRequestDto dto) {
        return ResponseEntity.ok(bookService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getById(id));
    }

    @GetMapping("/by-name")
    public ResponseEntity<BookDto> getByName(@RequestParam String title) {
        return ResponseEntity.ok(bookService.getByTitle(title));
    }

    @GetMapping("/authors")
    public ResponseEntity<List<String>> authorsByBookIds(
            @RequestParam List<Long> ids) {
        return ResponseEntity.ok(bookService.authorsByBookIds(ids));
    }
}
