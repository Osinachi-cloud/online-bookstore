package com.onlinebookstore.bookstore.service;

import com.onlinebookstore.bookstore.model.dto.request.BookRequest;
import com.onlinebookstore.bookstore.model.dto.response.BookResponse;
import com.onlinebookstore.bookstore.model.dto.response.HttpResponse;
import com.onlinebookstore.bookstore.model.entity.Book;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface BookService {

    HttpResponse<Page<BookResponse>> findAll(String title,
                                             String author,
                                             String yearOfPublication,
                                             String genre,
                                             int pageNumber,
                                             int pageSize);

    boolean bookExists(Long id);

    Book getBook(Long id);

    HttpResponse<BookResponse> createBook(BookRequest bookRequest);
}
