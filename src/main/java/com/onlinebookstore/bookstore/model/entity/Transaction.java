package com.onlinebookstore.bookstore.model.entity;

import com.onlinebookstore.bookstore.model.enums.PaymentMethod;
import com.onlinebookstore.bookstore.model.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction")
@ToString
public class Transaction extends BaseEntity {

    @Column(name = "reference")
    private String reference;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "gateway_response")
    private String gatewayResponse;

    @Column(name = "paid_at")
    private String paidAt;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "channel")
    private String channel;

    @Column(name = "currency")
    private String currency;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "wallet_transaction_id")
    private String walletTransactionId;

    @Column(name = "username")
    private String username;

    @Column(name = "payment_card_id")
    private String paymentCardId;

    @Column(name = "card_transaction_id")
    private String cardTransactionId;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "fee")
    private BigDecimal fee;

    @Column(name = "product_category")
    private String productCategory;

    @Column(name = "narration")
    private String narration;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

}
