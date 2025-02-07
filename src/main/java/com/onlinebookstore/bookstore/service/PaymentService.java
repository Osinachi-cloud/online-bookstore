package com.onlinebookstore.bookstore.service;

import com.onlinebookstore.bookstore.model.dto.request.InitializeTransactionRequest;
import com.onlinebookstore.bookstore.model.dto.response.InitializeTransactionResponse;
import com.onlinebookstore.bookstore.model.dto.response.PaymentVerificationResponse;

public interface PaymentService {

    InitializeTransactionResponse initTransaction(InitializeTransactionRequest paymentRequest) throws Exception;
    PaymentVerificationResponse paymentVerification(String reference) throws Exception;
}
