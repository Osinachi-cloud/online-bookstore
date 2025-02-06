package com.onlinebookstore.bookstore.model.dto.response;

import lombok.Data;

@Data
public class CartResponse {

    private Long id;

    private BookResponse book;

    private int quantity;

    private UserResponse user;

    private Double amount;

}