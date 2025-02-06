package com.onlinebookstore.bookstore.model.dto.request;

import lombok.Data;

@Data
public class CartItemRequest {

    private BookRequest book;
    private int quantity;
}
