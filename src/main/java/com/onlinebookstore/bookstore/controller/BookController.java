package com.onlinebookstore.bookstore.controller;

import com.onlinebookstore.bookstore.model.dto.request.BookRequest;
import com.onlinebookstore.bookstore.model.dto.request.CartRequest;
import com.onlinebookstore.bookstore.model.dto.response.BookResponse;
import com.onlinebookstore.bookstore.model.dto.response.CartResponse;
import com.onlinebookstore.bookstore.model.dto.response.HttpResponse;
import com.onlinebookstore.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

/**
 * <p>
 * This service is for managing a books, like creating and getting all books based on filter parameters.
 * </p>
 *
 * <p>
 * Author: Ogbodo Uchenna
 * </p>
 * <p>
 * Version: 1.0
 * </p>
 * <p>
 * Date: 2025-02-05
 * </p>
 */

@RestController
@RequestMapping(value = "/api/v1/books")
@Tag(name = "Book Controller", description = "Manage books in the bookstore")
@Slf4j
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Gets all books in the application and find by the following: title, author, isbn, genre, page or size, all optional.*
     * @return {@link BookResponse}, list of books in the application based on your filter parameters.
     */
    @GetMapping("")
    public ResponseEntity<HttpResponse<Page<BookResponse>>> getBooks(@RequestParam(required = false) String title,
                                                                     @RequestParam(required = false) String author,
                                                                     @RequestParam(required = false) String yearOfPublication,
                                                                     @RequestParam(required = false) String genre,
                                                                     @RequestParam Optional<Integer> page,
                                                                     @RequestParam Optional<Integer> size){
        return ResponseEntity.ok(bookService.findAll(title, author,yearOfPublication, genre, page.orElse(0), size.orElse(10) ));
    }

    /**
     * Creates a book object*
     * Passes {@link BookRequest} as a request body.*
     * @return the {@link BookResponse} object.
     */
    @PostMapping("/create-book")
    public ResponseEntity<HttpResponse<BookResponse>> createBook(@Valid @RequestBody BookRequest bookRequest){
        log.info("bookRequest : {}", bookRequest);
         return ResponseEntity.ok(bookService.createBook(bookRequest));
    }



}
