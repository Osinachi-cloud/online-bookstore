package com.onlinebookstore.bookstore.controller;

import com.onlinebookstore.bookstore.exceptions.BookStoreException;
import com.onlinebookstore.bookstore.model.dto.request.InitializeTransactionRequest;
import com.onlinebookstore.bookstore.model.dto.response.InitializeTransactionResponse;
import com.onlinebookstore.bookstore.model.dto.response.PaymentVerificationResponse;
import com.onlinebookstore.bookstore.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * This service manages payment initialization and verification
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
@RequestMapping("/api/v1/payment")
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Passes {@link InitializeTransactionRequest} to initialize payment.*
     * @return a {@link InitializeTransactionResponse} object which contains the reference.
     * @throws BookStoreException if an error occurs while calling the endpoint.
     */
    @PostMapping(value = "/initialize")
    @Transactional
    public InitializeTransactionResponse initializePayment(@RequestBody InitializeTransactionRequest paymentRequest){
        log.info("paymentRequest : {}", paymentRequest);

        try {
            return paymentService.initTransaction(paymentRequest);
        }catch (BookStoreException e){
            throw new BookStoreException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Passes the reference gotten from initialize payment endpoint.*
     * @return a {@link PaymentVerificationResponse} object.
     * @throws BookStoreException if an error occurs while calling the endpoint.
     */
    @PostMapping(value = "/verify")
    @Transactional
    public PaymentVerificationResponse verifyPayment(@RequestParam String reference){
        try {
            return paymentService.paymentVerification(reference);
        }catch (BookStoreException e){
            throw new BookStoreException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
