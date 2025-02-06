package com.onlinebookstore.bookstore.model.dto.response;

import lombok.Data;

@Data
public class CartItemResponse {

    private BookResponse book;
    private int quantity;
}
