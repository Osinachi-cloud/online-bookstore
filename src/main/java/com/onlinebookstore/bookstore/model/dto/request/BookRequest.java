package com.onlinebookstore.bookstore.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.onlinebookstore.bookstore.model.enums.Genre;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BookRequest {

    @NotBlank
    @JsonProperty("title")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Title must contain only letters and numbers")
    private String title;

    @NotBlank
    @JsonProperty("genre")
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @NotBlank
    @JsonProperty("isbn")
    @Pattern(regexp = "^[0-9-]+$", message = "ISBN must contain only numbers and dashes")
    private String isbn;

    @NotBlank
    @JsonProperty("author")
    private String author;

    @NotBlank
    @JsonProperty("yearOfPublication")
    private String yearOfPublication;

    @NotBlank
    @JsonProperty("price")
    private double price;
}
