package com.onlinebookstore.bookstore.controller;

import com.onlinebookstore.bookstore.model.dto.request.BookRequest;
import com.onlinebookstore.bookstore.model.dto.response.BookResponse;
import com.onlinebookstore.bookstore.model.dto.response.HttpResponse;
import com.onlinebookstore.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/books")
@Tag(name = "Book Controller", description = "Manage books in the bookstore")
@Slf4j
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("")
    public ResponseEntity<HttpResponse<Page<BookResponse>>> getBooks(@RequestParam(required = false) String title,
                                                                     @RequestParam(required = false) String author,
                                                                     @RequestParam(required = false) String yearOfPublication,
                                                                     @RequestParam(required = false) String genre,
                                                                     @RequestParam Optional<Integer> page,
                                                                     @RequestParam Optional<Integer> size){
        return ResponseEntity.ok(bookService.findAll(title, author,yearOfPublication, genre, page.orElse(0), size.orElse(10) ));
    }

    @PostMapping("/create-book")
    public ResponseEntity<HttpResponse<BookResponse>> createBook(@Valid @RequestBody BookRequest bookRequest){
        log.info("bookRequest : {}", bookRequest);
         return ResponseEntity.ok(bookService.createBook(bookRequest));
    }



}
