package com.onlinebookstore.bookstore.model.dto.response;

import com.onlinebookstore.bookstore.model.enums.Genre;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class BookResponse {

    private String title;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private String isbn;

    private String author;

    private String yearOfPublication;

    private Double price;
}
