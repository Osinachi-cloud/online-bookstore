package com.onlinebookstore.bookstore.model.dto.response;

import com.onlinebookstore.bookstore.model.enums.PaymentMethod;
import com.onlinebookstore.bookstore.model.enums.TransactionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.util.List;

@Data
public class TransactionResponse {

    private String reference;

    private Double amount;

    private String paidAt;

    private String transactionId;

    private String username;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private List<BookOrderResponse> bookOrderList;
}
