package com.onlinebookstore.bookstore.service;

import com.onlinebookstore.bookstore.model.dto.request.InitializeTransactionRequest;
import com.onlinebookstore.bookstore.model.dto.request.Order;
import com.onlinebookstore.bookstore.model.entity.BookOrder;

import java.util.List;

public interface OrderService {
    Order initializeOrder(InitializeTransactionRequest orderRequest);

    BookOrder save(BookOrder productBookOrder);

    List<BookOrder> getOrdersByTransactionId(String transactionId);
}
