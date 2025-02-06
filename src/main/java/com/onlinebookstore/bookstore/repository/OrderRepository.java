package com.onlinebookstore.bookstore.repository;

import com.onlinebookstore.bookstore.model.entity.BookOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<BookOrder, Long> {
    List<BookOrder> findOrdersByTransactionId(String orderId);
}
