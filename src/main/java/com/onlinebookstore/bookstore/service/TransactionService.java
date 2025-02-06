package com.onlinebookstore.bookstore.service;

import com.onlinebookstore.bookstore.model.dto.response.HttpResponse;
import com.onlinebookstore.bookstore.model.dto.response.TransactionResponse;
import com.onlinebookstore.bookstore.model.entity.Transaction;
import org.springframework.data.domain.Page;

public interface   TransactionService {

    Transaction saveTransaction(Transaction transaction);

    Transaction findByReference(String reference);

    boolean existsByReference(String reference);

    HttpResponse<Page<TransactionResponse>> getTransactionBy(String username, String reference, int pageNum, int pageSize);
}
