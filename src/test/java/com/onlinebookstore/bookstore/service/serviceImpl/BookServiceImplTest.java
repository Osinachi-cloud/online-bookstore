package com.onlinebookstore.bookstore.service.serviceImpl;

import com.onlinebookstore.bookstore.exceptions.BookStoreException;
import com.onlinebookstore.bookstore.model.dto.request.BookRequest;
import com.onlinebookstore.bookstore.model.dto.response.BookResponse;
import com.onlinebookstore.bookstore.model.dto.response.HttpResponse;
import com.onlinebookstore.bookstore.model.entity.Book;
import com.onlinebookstore.bookstore.model.enums.Genre;
import com.onlinebookstore.bookstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository mockBookRepository;

    private BookServiceImpl bookServiceImplUnderTest;

    @BeforeEach
    void setUp() {
        bookServiceImplUnderTest = new BookServiceImpl(mockBookRepository);
    }

    @Test
    void testFindAll() {
        final Book book = new Book();
        book.setTitle("American Psycho");
        book.setGenre(Genre.FICTION);
        book.setIsbn("9780061120084");
        book.setAuthor("Agatha Christie");
        book.setYearOfPublication("1897");
        book.setPrice(0.0);
        final Page<Book> bookPage = new PageImpl<>(List.of(book));
        when(mockBookRepository.findAllBy(eq("title"), eq("author"), eq("yearOfPublication"), eq("genre"),
                any(Pageable.class))).thenReturn(bookPage);

        final HttpResponse<Page<BookResponse>> result = bookServiceImplUnderTest.findAll("title", "author",
                "yearOfPublication", "genre", 0, 10);
    }


    @Test
    void testCreateBook_ThrowsBookStoreException() {
        final BookRequest bookRequest = new BookRequest();
        bookRequest.setTitle("American Psycho");
        bookRequest.setGenre(Genre.FICTION);
        bookRequest.setIsbn("9780061120084");
        bookRequest.setAuthor("Agatha Christie");
        bookRequest.setYearOfPublication("1897");
        bookRequest.setPrice(0.0);

        final Book book = new Book();
        book.setTitle("American Psycho");
        book.setGenre(Genre.FICTION);
        book.setIsbn("9780061120084");
        book.setAuthor("Agatha Christie");
        book.setYearOfPublication("1897");
        book.setPrice(0.0);
        when(mockBookRepository.findByIsbn("9780061120084")).thenReturn(book);

        assertThatThrownBy(() -> bookServiceImplUnderTest.createBook(bookRequest))
                .isInstanceOf(BookStoreException.class);
    }

    @Test
    void testCreateBook() {
        final BookRequest bookRequest = new BookRequest();
        bookRequest.setTitle("American Psycho");
        bookRequest.setGenre(Genre.FICTION);
        bookRequest.setIsbn("9780061120084");
        bookRequest.setAuthor("Agatha Christie");
        bookRequest.setYearOfPublication("1897");
        bookRequest.setPrice(0.0);

        final BookResponse bookResponse = new BookResponse();
        bookResponse.setTitle("American Psycho");
        bookResponse.setGenre(Genre.FICTION);
        bookResponse.setIsbn("9780061120084");
        bookResponse.setAuthor("Agatha Christie");
        bookResponse.setYearOfPublication("1897");
        bookResponse.setPrice(0.0);

        final HttpResponse<BookResponse> expectedResult = new HttpResponse<>("05-01-2025", 201, HttpStatus.OK, "message", bookResponse);

        when(mockBookRepository.findByIsbn("9780061120084")).thenReturn(null);

        when(mockBookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final HttpResponse<BookResponse> result = bookServiceImplUnderTest.createBook(bookRequest);
        result.setTimeStamp("05-01-2025");
        result.setStatus(HttpStatus.OK);
        result.setMessage("message");

        assertThat(result).isEqualTo(expectedResult);
    }
}
