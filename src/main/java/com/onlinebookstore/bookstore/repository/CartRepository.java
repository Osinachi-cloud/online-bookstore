package com.onlinebookstore.bookstore.repository;

import com.onlinebookstore.bookstore.model.entity.Book;
import com.onlinebookstore.bookstore.model.entity.Cart;
import com.onlinebookstore.bookstore.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserAndBook(User user, Book book);

    List<Cart> findByUser(User user);

    void deleteByUser(User user);


}
