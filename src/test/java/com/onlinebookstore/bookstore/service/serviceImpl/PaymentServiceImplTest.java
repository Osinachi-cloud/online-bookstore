package com.onlinebookstore.bookstore.service.serviceImpl;

import com.onlinebookstore.bookstore.exceptions.BookStoreException;
import com.onlinebookstore.bookstore.model.dto.request.InitializeTransactionRequest;
import com.onlinebookstore.bookstore.model.dto.request.Order;
import com.onlinebookstore.bookstore.model.dto.response.InitializeTransactionResponse;
import com.onlinebookstore.bookstore.model.dto.response.PaymentVerificationResponse;
import com.onlinebookstore.bookstore.model.entity.BookOrder;
import com.onlinebookstore.bookstore.model.entity.User;
import com.onlinebookstore.bookstore.model.enums.PaymentMethod;
import com.onlinebookstore.bookstore.service.OrderService;
import com.onlinebookstore.bookstore.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

    @Mock
    private OrderService orderService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Value("${paystack.call-back-url}")
    private String callBackURL = "http://localhost/callback";

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testInitTransaction_Success() throws Exception {
        // Arrange
        InitializeTransactionRequest request = new InitializeTransactionRequest();
        request.setCardPan("1234567812345678"); // Valid card number
        request.setUsername("testUser ");

        Order order = new Order();
        order.setTransactionId("TRANSACTION_ID");
        order.setOrderTotal(100.0);
        when(orderService.initializeOrder(any())).thenReturn(order);

        InitializeTransactionResponse response = paymentService.initTransaction(request);

        assertNotNull(response);
        assertTrue(response.isStatus());
        assertEquals("Transaction initialized successfully.", response.getMessage());
        assertEquals("TRANSACTION_ID", response.getData().getReference());
    }

    @Test
    public void testInitTransaction_Failure_InvalidCard() throws Exception {
        InitializeTransactionRequest request = new InitializeTransactionRequest();
        request.setCardPan("INVALID_CARD");
        request.setUsername("testUser ");

        Order order = new Order();
        order.setTransactionId("TRANSACTION_ID");
        order.setOrderTotal(100.0);
        when(orderService.initializeOrder(any())).thenReturn(order);

        InitializeTransactionResponse response = paymentService.initTransaction(request);

        assertNotNull(response);
        assertFalse(response.isStatus());
        assertEquals("Transaction failed.", response.getMessage());
    }

    @Test
    public void testPaymentVerification_Success() throws Exception {
        String reference = "TRANSACTION_ID";
        BookOrder bookOrder = new BookOrder();
        bookOrder.setAmount(100.0);
        bookOrder.setTransactionId(reference);
        bookOrder.setPaymentMethod(PaymentMethod.WEB);
        User user = new User();
        user.setUsername("username");
        bookOrder.setUser(user);

        when(orderService.getOrdersByTransactionId(reference)).thenReturn(List.of(bookOrder));
        when(transactionService.existsByReference(reference)).thenReturn(false);

        PaymentVerificationResponse response = paymentService.paymentVerification(reference);

        assertNotNull(response);
        assertTrue(response.getStatus());
        assertEquals("Verification successful", response.getMessage());
    }

    @Test
    public void testPaymentVerification_NoReferenceFound() {
        String reference = "INVALID_TRANSACTION_ID";
        when(orderService.getOrdersByTransactionId(reference)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(BookStoreException.class, () -> {
            paymentService.paymentVerification(reference);
        });
        assertEquals("No reference found for this order", exception.getMessage());
    }

    @Test
    public void testPaymentVerification_TransactionAlreadyExists() {
        String reference = "TRANSACTION_ID";
        BookOrder bookOrder = new BookOrder();
        bookOrder.setTransactionId(reference);
        when(orderService.getOrdersByTransactionId(reference)).thenReturn(List.of(bookOrder));
        when(transactionService.existsByReference(reference)).thenReturn(true);

        Exception exception = assertThrows(BookStoreException.class, () -> {
            paymentService.paymentVerification(reference);
        });
        assertEquals("Transaction reference already exists", exception.getMessage());
    }
}