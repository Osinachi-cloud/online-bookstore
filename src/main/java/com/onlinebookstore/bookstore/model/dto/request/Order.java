package com.onlinebookstore.bookstore.model.dto.request;

import com.onlinebookstore.bookstore.model.entity.BookOrder;
import com.onlinebookstore.bookstore.model.enums.PaymentMethod;
import lombok.Data;

import java.util.List;

@Data
public class Order {
    private String username;
    private String transactionId;
    private List<BookOrder> orderList;
    private Double orderTotal;
    private PaymentMethod paymentMethod;
}
