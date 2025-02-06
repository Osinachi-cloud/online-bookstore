package com.onlinebookstore.bookstore.model.dto.request;

import lombok.Data;

@Data
public class CartRequest {
    private Long bookId;
    private int quantity;
//    private String username;
//    private Double amount;
}
