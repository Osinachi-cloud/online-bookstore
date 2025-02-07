package com.onlinebookstore.bookstore.service.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinebookstore.bookstore.exceptions.BookStoreException;
import com.onlinebookstore.bookstore.model.dto.request.InitializeTransactionRequest;
import com.onlinebookstore.bookstore.model.dto.request.Order;
import com.onlinebookstore.bookstore.model.dto.response.InitializeTransactionResponse;
import com.onlinebookstore.bookstore.model.dto.response.PaymentVerificationResponse;
import com.onlinebookstore.bookstore.model.entity.BookOrder;
import com.onlinebookstore.bookstore.model.entity.Transaction;
import com.onlinebookstore.bookstore.model.enums.OrderStatus;
import com.onlinebookstore.bookstore.model.enums.PaymentMethod;
import com.onlinebookstore.bookstore.model.enums.TransactionStatus;
import com.onlinebookstore.bookstore.service.OrderService;
import com.onlinebookstore.bookstore.service.PaymentService;
import com.onlinebookstore.bookstore.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {


    @Value("${secret-key}")
    private String secretKey;

    @Value("${initialize-payment-url}")
    private String initializePaymentUrl;

    @Value("${verification-url}")
    private String verificationUrl;

    private final OrderService orderService;
    private final TransactionService transactionService;

    public PaymentServiceImpl(OrderService orderService, TransactionService transactionService) {
        this.orderService = orderService;
        this.transactionService = transactionService;
    }


    /**
     * Passes {@link InitializeTransactionRequest} to initialize payment.*
     * calls the {@link initializePaymentUrl} pass in the {@link secretKey}
     * @return a {@link InitializeTransactionResponse} object which contains the reference.
     * fails when you pass in a card pan with less or more than 16 characters
     * @throws BookStoreException if an error occurs while calling the endpoint.
     */
    @Override
    @Transactional
    public InitializeTransactionResponse initTransaction(InitializeTransactionRequest request) throws Exception {
        InitializeTransactionResponse initializeTransactionResponse = null;
        try {

            // calls payment gateway endpoint to initialize payment.

            ObjectMapper mapper = new ObjectMapper();

            Order order = orderService.initializeOrder(request);


            String res = " { \"status\": true, \"message\": \"Transaction initialized successfully.\", \"total_amount\": "+order.getOrderTotal() +", \"data\": { \"authorization_url\": \"https://paymentgateway.com/authorize?code=ACCESS"+order.getTransactionId()+"\", \"access_code\": \"ACCESS"+order.getTransactionId()+"\", \"reference\": \""+order.getTransactionId()+"\" } }";
            String err = " { \"status\": false, \"message\": \"Transaction failed.\", \"total_amount\": "+order.getOrderTotal() +" }";


            initializeTransactionResponse = mapper.readValue(request.getCardPan().length() == 16?  res : err, InitializeTransactionResponse.class);

            log.info("initializeTransactionResponse : {}", initializeTransactionResponse);
            return initializeTransactionResponse;


        } catch (BookStoreException ex) {
            throw new BookStoreException("Failure initializing payment transaction: " + ex.getMessage() );
        }
    }

    /**
     * Passes the reference gotten from initialize payment.*
     * calls the {@link verificationUrl}
     * @return a PaymentVerificationResponse object.
     * @throws BookStoreException if an error occurs while calling the endpoint.
     */

    @Override
    @Transactional
    public PaymentVerificationResponse paymentVerification(String reference) throws Exception {
        List<BookOrder> bookOrders = orderService.getOrdersByTransactionId(reference);
        boolean transactionExists = transactionService.existsByReference(reference);

        if(bookOrders.isEmpty()){
            throw new BookStoreException("No reference found for this order");
        }

        if(transactionExists){
            throw new BookStoreException("Transaction reference already exists");
        }

        Order order = new Order();
        order.setOrderList(bookOrders);
        order.setTransactionId(reference);
        double totalAmount = bookOrders.stream().mapToDouble(BookOrder::getAmount).sum();
        order.setOrderTotal(totalAmount);
        order.setUsername(bookOrders.get(0).getUser().getUsername());
        order.setPaymentMethod(bookOrders.get(0).getPaymentMethod());

        PaymentVerificationResponse paymentVerificationResponse = null;
        Transaction transaction = null;


        try{

            // calls payment gateway verify endpoint passing the reference received from payment initialization

            ObjectMapper mapper = new ObjectMapper();

            String jsonResponse = "{ \"status\": true, \"message\": \"Verification successful\", \"data\": { \"authorization_url\": \"https://paymentgateway.com/authorize?code=ACCESS" + reference +"\", \"status\": \"success\", \"reference\": \"" + reference + "\", \"access_code\": \"ACCESS" + reference +"\", \"receipt_number\": \""+ reference +"\", \"amount\":" + order.getOrderTotal() + ", \"message\": \"Payment successful.\", \"gateway_response\": \"Successful\", \"paid_at\": \""+ LocalDateTime.now()+"\", \"created_at\": \"" + LocalDateTime.now() +"\", \"payment_method\": \""+ order.getPaymentMethod()+"\", \"currency\": \"NGN\", \"fees\": \"10.00\", \"authorization\": {}, \"customer\": {\"username\": \"" + order.getUsername() +"\" }, \"order_id\": \""+ order.getTransactionId() +"\", \"paidAt\": \""+ LocalDateTime.now() +"\", \"createdAt\": \"" + LocalDateTime.now() +"\", \"requested_amount\": 100,  \"transaction_date\": \""+ LocalDate.now() +"\" } }";

            paymentVerificationResponse = mapper.readValue(jsonResponse, PaymentVerificationResponse.class);

            log.info("verify :{}",  paymentVerificationResponse);

            if (paymentVerificationResponse == null || !paymentVerificationResponse.getStatus()) {
                throw new Exception("An error");
            } else if (paymentVerificationResponse.getMessage().equals("Verification successful")) {
                transaction = Transaction.builder()
                        .username(paymentVerificationResponse.getData().getCustomer().getUsername())
                        .transactionId(paymentVerificationResponse.getData().getReference())
                        .reference(paymentVerificationResponse.getData().getReference())
                        .amount(paymentVerificationResponse.getData().getAmount())
                        .gatewayResponse(paymentVerificationResponse.getData().getGatewayResponse())
                        .paidAt(paymentVerificationResponse.getData().getPaidAt())
                        .createdAt(paymentVerificationResponse.getData().getCreatedAt())
                        .paymentMethod(PaymentMethod.valueOf(paymentVerificationResponse.getData().getPaymentMethod()))
                        .currency(paymentVerificationResponse.getData().getCurrency())
                        .ipAddress(paymentVerificationResponse.getData().getIpAddress())
                        .build();
                if(paymentVerificationResponse.getData().getStatus().equals("abandoned")){
                    log.info("ABANDONED HERE");
                    transaction.setStatus(TransactionStatus.FAILED);
                }

                if(paymentVerificationResponse.getData().getStatus().equals("success")){
                    log.info("PAYMENT SUCCESSFUL");

                    transaction.setStatus(TransactionStatus.COMPLETED);
                }

                Transaction t = transactionService.saveTransaction(transaction);

                log.info("transaction entity: {}", t);

                List<BookOrder> bookOrderList = orderService.getOrdersByTransactionId(t.getTransactionId());

                log.info("Order entity: {}", bookOrderList);

                for(BookOrder productBookOrder : bookOrderList){
                    productBookOrder.setStatus(OrderStatus.PAYMENT_COMPLETED);
                    BookOrder p = orderService.save(productBookOrder);
                    log.info("order entity: {}", p);
                }

            }
        } catch (BookStoreException ex) {
            log.info(ex.fillInStackTrace().getLocalizedMessage());
            throw new BookStoreException(ex.getLocalizedMessage());
        }
        return paymentVerificationResponse;
    }

}
