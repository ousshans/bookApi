package com.renault.bookapi.service;

import com.renault.bookapi.dto.BookDto;
import com.renault.bookapi.dto.BookRequestDto;
import com.renault.bookapi.dto.BookTitleDto;
import com.renault.bookapi.entity.Author;
import com.renault.bookapi.entity.Book;
import com.renault.bookapi.exception.ResourceNotFoundException;
import com.renault.bookapi.mapper.BookMapper;
import com.renault.bookapi.repository.AuthorRepository;
import com.renault.bookapi.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookService {
    private final BookRepository bookRepo;
    private final BookMapper mapper;
    private final AuthorRepository authorRepo;

    @Value("${openlibrary.url}")
    private String openLibraryUrl;

    @Autowired
    private RestTemplate restTemplate;


    public BookService(BookRepository bookRepo,
                       AuthorRepository authorRepo,
                       BookMapper mapper) {
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
        this.mapper = mapper;
    }

    public BookDto create(BookRequestDto dto) {
        Author author = authorRepo.findByName(dto.authorName())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Author with name \"" + dto.authorName() + "\" doesn't exist")
                );

        Book book = new Book();
        book.setTitle(dto.title());
        book.setPublicationDate(dto.publicationDate());
        book.setType(dto.type());
        book.setAuthor(author);

        return mapper.toDto(bookRepo.save(book));
    }

    public BookDto update(Long id, BookRequestDto dto) {
        Book existing = bookRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        existing.setTitle(dto.title());
        existing.setPublicationDate(dto.publicationDate());
        existing.setType(dto.type());
        return mapper.toDto(bookRepo.save(existing));
    }

    public void delete(Long id) {
        bookRepo.deleteById(id);
    }

    public BookDto getById(Long id) {
        return mapper.toDto(bookRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found")));
    }

    public BookDto getByTitle(String title) {
        return mapper.toDto(bookRepo.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found")));
    }

    public List<String> authorsByBookIds(List<Long> ids) {
        return bookRepo.findAllById(ids).stream()
                .map(b -> b.getAuthor().getName())
                .distinct()
                .collect(Collectors.toList());
    }

    public double rating(Long id) {
        Book book = bookRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        int age = Period.between(book.getPublicationDate(), LocalDate.now()).getYears();
        double recentScore = Math.max(0, 10 - age);
        double authorScore = book.getAuthor().getFollowersNumber() / 1000.0;
        return Math.min(10, recentScore + authorScore);
    }

    public BookTitleDto getByIsbn(String isbn) {
        String url = String.format(
                "%s?bibkeys=ISBN:%s&format=json&jscmd=data",
                openLibraryUrl,
                isbn
        );
        Map<String, Object> resp = restTemplate.getForObject(url, Map.class);
        if (resp == null || !resp.containsKey("ISBN:" + isbn)) {
            throw new ResourceNotFoundException("Book not found for ISBN " + isbn);
        }
        Map<String, Object> data = (Map<String, Object>) resp.get("ISBN:" + isbn);
        String title = (String) data.get("title");
        System.out.println(title);
        return new BookTitleDto(title);
    }
}
