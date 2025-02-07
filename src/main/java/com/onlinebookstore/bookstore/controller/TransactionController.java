package com.onlinebookstore.bookstore.controller;

import com.onlinebookstore.bookstore.exceptions.BookStoreException;
import com.onlinebookstore.bookstore.model.dto.request.InitializeTransactionRequest;
import com.onlinebookstore.bookstore.model.dto.response.HttpResponse;
import com.onlinebookstore.bookstore.model.dto.response.InitializeTransactionResponse;
import com.onlinebookstore.bookstore.model.dto.response.TransactionResponse;
import com.onlinebookstore.bookstore.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


/**
 * <p>
 * This service searches for your purchase history
 * </p>
 *
 * <p>
 * Author: Ogbodo Uchenna
 * </p>
 * <p>
 * Version: 1.0
 * </p>
 * <p>
 * Date: 2025-02-05
 * </p>
 */

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Passes username to get your payment history as a request parameter.*
     * @return a list of {@link TransactionResponse} object.
     */
    @GetMapping("")
    public ResponseEntity<HttpResponse<Page<TransactionResponse>>> getTransactionByUser(@RequestParam String username,
                                                                                        @RequestParam(required = false) String reference,
                                                                                        @RequestParam Optional<Integer> page,
                                                                                        @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(transactionService.getTransactionBy(username, reference, page.orElse(0), size.orElse(10) ));
    }
}
