package com.onlinebookstore.bookstore.model.dto.response;

import com.onlinebookstore.bookstore.model.enums.OrderStatus;
import lombok.Data;

@Data
public class BookOrderResponse {

    private String orderId;

    private BookResponse bookResponse;

    private int quantity;

    private UserResponse user;

    private Double amount;

    private OrderStatus status;
}
