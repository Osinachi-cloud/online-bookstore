package com.onlinebookstore.bookstore.model.entity;


import com.onlinebookstore.bookstore.model.enums.OrderStatus;
import com.onlinebookstore.bookstore.model.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_order")
public class BookOrder extends BaseEntity{

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private boolean isPaid;

    @Column(name = "order_id", unique = true, nullable = false)
    private String orderId;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    @Column(name = "amount")
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookOrder)) return false;
        BookOrder that = (BookOrder) o;
        return quantity == that.quantity &&
                Double.compare(that.amount, amount) == 0 &&
                Objects.equals(orderDate, that.orderDate) &&
                paymentMethod == that.paymentMethod &&
                Objects.equals(orderId, that.orderId) &&
                Objects.equals(transactionId, that.transactionId) &&
                Objects.equals(book, that.book) &&
                Objects.equals(user, that.user) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderDate, paymentMethod, orderId, transactionId, book, quantity, user, amount, status);
    }
}
