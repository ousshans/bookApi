package com.renault.bookapi.service;

import com.renault.bookapi.dto.AuthorDto;
import com.renault.bookapi.dto.BookDto;
import com.renault.bookapi.dto.BookRequestDto;
import com.renault.bookapi.dto.BookTitleDto;
import com.renault.bookapi.entity.Author;
import com.renault.bookapi.entity.Book;
import com.renault.bookapi.exception.ResourceNotFoundException;
import com.renault.bookapi.mapper.BookMapper;
import com.renault.bookapi.repository.AuthorRepository;
import com.renault.bookapi.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    BookRepository bookRepo;
    @Mock
    AuthorRepository authorRepo;
    @Mock
    BookMapper mapper;
    @Mock private RestTemplate restTemplate;
    @InjectMocks BookService service;

    private Author author;
    private Book book;
    private BookDto bookDto;
    private BookRequestDto requestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Inject the OpenLibrary URL into the private field
        ReflectionTestUtils.setField(
                service, "openLibraryUrl", "https://openlibrary.org/api/books"
        );

        author = new Author();
        author.setId(1L);
        author.setName("Victor Hugo");
        author.setAge(80);
        author.setFollowersNumber(120000);

        book = new Book();
        book.setId(10L);
        book.setTitle("Les Misérables");
        book.setPublicationDate(LocalDate.of(1862,1,1));
        book.setType("Roman");
        book.setAuthor(author);

        bookDto = new BookDto(10L, "Les Misérables","Roman" ,LocalDate.of(1862,1,1),
                new AuthorDto(1L,"Victor Hugo",80,120000));

        requestDto = new BookRequestDto("Les Misérables", LocalDate.of(1862,1,1),
                "Roman","Victor Hugo");
    }

    @Test
    void create_success() {
        when(authorRepo.findByName("Victor Hugo")).thenReturn(Optional.of(author));
        when(bookRepo.save(any(Book.class))).thenReturn(book);
        when(mapper.toDto(book)).thenReturn(bookDto);

        BookDto result = service.create(requestDto);

        assertEquals(10L, result.id());
        assertEquals("Les Misérables", result.title());
        verify(bookRepo).save(any());
    }

    @Test
    void create_authorNotFound() {
        when(authorRepo.findByName("Victor Hugo")).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.create(requestDto));
        assertTrue(ex.getMessage().contains("doesn't exist"));
    }

    @Test
    void getById_found() {
        when(bookRepo.findById(10L)).thenReturn(Optional.of(book));
        when(mapper.toDto(book)).thenReturn(bookDto);

        BookDto dto = service.getById(10L);
        assertEquals("Les Misérables", dto.title());
    }

    @Test
    void getById_notFound() {
        when(bookRepo.findById(10L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(10L));
    }

    @Test
    void getByTitle_found() {
        when(bookRepo.findByTitle("Les Misérables")).thenReturn(Optional.of(book));
        when(mapper.toDto(book)).thenReturn(bookDto);

        BookDto dto = service.getByTitle("Les Misérables");
        assertEquals(10L, dto.id());
    }

    @Test
    void update_success() {
        when(bookRepo.findById(10L)).thenReturn(Optional.of(book));
        when(bookRepo.save(book)).thenReturn(book);
        when(mapper.toDto(book)).thenReturn(bookDto);

        BookDto dto = service.update(10L, requestDto);
        assertEquals("Les Misérables", dto.title());
    }

    @Test
    void update_notFound() {
        when(bookRepo.findById(10L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> service.update(10L, requestDto));
    }

    @Test
    void delete_callsRepository() {
        doNothing().when(bookRepo).deleteById(10L);
        service.delete(10L);
        verify(bookRepo).deleteById(10L);
    }

    @Test
    void authorsByBookIds_distinctNames() {
        Book b2 = new Book();
        b2.setAuthor(author);
        when(bookRepo.findAllById(List.of(10L,11L)))
                .thenReturn(List.of(book, b2));

        List<String> authors = service.authorsByBookIds(List.of(10L,11L));
        assertEquals(1, authors.size());
        assertEquals("Victor Hugo", authors.get(0));
    }

    @Test
    void testRating_computation() {
        // Age >> 10 years ⇒ recentScore = 0; authorScore = 120000/1000 = 120 ⇒ total capped at 10
        when(bookRepo.findById(10L)).thenReturn(Optional.of(book));

        double rating = service.rating(10L);
        assertEquals(10.0, rating, "Rating should be capped at 10");
    }

}