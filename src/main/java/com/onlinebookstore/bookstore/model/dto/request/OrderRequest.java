package com.onlinebookstore.bookstore.model.dto.request;

import com.onlinebookstore.bookstore.model.enums.PaymentMethod;
import lombok.Data;

@Data
public class OrderRequest {
    private String cardPan;
    private PaymentMethod paymentMethod;
    private String username;
}
