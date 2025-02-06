package com.onlinebookstore.bookstore.service.serviceImpl;

import com.onlinebookstore.bookstore.exceptions.BookStoreException;
import com.onlinebookstore.bookstore.model.dto.request.BookRequest;
import com.onlinebookstore.bookstore.model.dto.response.BookResponse;
import com.onlinebookstore.bookstore.model.dto.response.HttpResponse;
import com.onlinebookstore.bookstore.model.entity.Book;
import com.onlinebookstore.bookstore.repository.BookRepository;
import com.onlinebookstore.bookstore.service.BookService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;
import static com.onlinebookstore.bookstore.model.enums.Genre.*;
import static com.onlinebookstore.bookstore.utils.ModelMapper.*;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public HttpResponse<Page<BookResponse>> findAll(String title,
                                String author,
                                String yearOfPublication,
                                String genre,
                                int pageNumber,
                                int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Book> bookPage = bookRepository.findAllBy(title, author, yearOfPublication, genre, page);
        List<BookResponse> bookResponseList = mapListOfBooksToBookResponse(bookPage.getContent());
        Page<BookResponse> bookResponsePage = new PageImpl<>(bookResponseList, page, bookResponseList.size());

        log.info("bookResponseList : {}",bookResponseList);
        return HttpResponse.<Page<BookResponse>>builder()
                .message("successful")
                .statusCode(200)
                .status(HttpStatus.OK)
                .data(bookResponsePage)
                .build();
    }

    @Override
    public boolean bookExists(Long id){
        Optional<Book> bookExists = bookRepository.findBookById(id);
        return bookExists.isPresent();
    }

    @Override
    public Book getBook(Long id){
        Optional<Book> bookExists = bookRepository.findBookById(id);
        if(bookExists.isEmpty()){
            throw new BookStoreException("Book not available");
        }
        return bookExists.get();
    }

    public void validateBookRequest(BookRequest bookRequest){
        if (!bookRequest.getIsbn().matches("^[0-9-]+$")) {
            log.error("Invalid ISBN: {}", bookRequest.getIsbn());
            throw new BookStoreException("Invalid ISBN. Must contain only numbers and dashes");
        }

        Book existingBook = bookRepository.findByIsbn(bookRequest.getIsbn());
        if (existingBook != null) {
            throw new BookStoreException("A book with the same ISBN already exists.");
        }
    }

    @Override
    public HttpResponse<BookResponse> createBook(BookRequest bookRequest){
        log.info("bookRequest : {}", bookRequest);
        validateBookRequest(bookRequest);

        Book book = mapBookRequestToEntity(bookRequest);
        log.info("book ===>> : {}", book);
        Book savedBook = bookRepository.save(book);
        BookResponse bookResponse = mapBookModelToResponse(savedBook);

        return HttpResponse.<BookResponse>builder()
                .status(HttpStatus.CREATED)
                .data(bookResponse)
                .statusCode(201)
                .message("Book Created")
                .build();
    }

    @PostConstruct
    public void preloadBooks(){
        if(bookRepository.count() < 1) {
            Book book1 = new Book("To Kill a Mockingbird", FICTION, "9780061120084", "Harper Lee", "1960", 50.00);
            Book book2 = new Book("Gone Girl", THRILLER, "9780307588371", "Gillian Flynn", "2012", 60.00);
            Book book3 = new Book("The Girl with the Dragon Tattoo", MYSTERY, "9780307949486", "Stieg Larsson", "2005", 70.00);
            Book book4 = new Book("The Waste Land", POETRY, "9780571202405", "T.S. Eliot", "1922", 80.00);
            Book book5 = new Book("Dracula", HORROR, "9780486411095", "Bram Stoker", "1897", 90.00);
            Book book6 = new Book("American Psycho", SATIRE, "9780679735779", "Bret Easton Ellis", "1991", 100.00);
            Book book7 = new Book("The Shining", HORROR, "9780307743657", "Stephen King", "1977", 110.00);
            Book book8 = new Book("The Silence of the Lambs", THRILLER, "9780312924584", "Thomas Harris", "1988", 120.00);
            Book book9 = new Book("And Then There Were None", MYSTERY, "9780062073488", "Agatha Christie", "1939", 130.00);
            Book book10 = new Book("Catch-22", SATIRE, "9781451626650", "Joseph Heller", "1961", 140.00);

            List<Book> bookList = List.of(book1, book2, book3, book4, book5, book6, book7, book8, book9, book10);

            bookRepository.saveAll(bookList);
        }
    }
}
