package com.onlinebookstore.bookstore.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlinebookstore.bookstore.model.enums.PaymentMethod;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class InitializeTransactionRequest {

    private String username;
    private String cardPan;
    private PaymentMethod paymentMethod;
}
