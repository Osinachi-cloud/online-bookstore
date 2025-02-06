package com.onlinebookstore.bookstore.repository;

import com.onlinebookstore.bookstore.model.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByReference(String reference);


    @Query(value = "SELECT * FROM Transaction " +
            "WHERE (:username IS NULL OR username = :username) " +
            "AND (:reference IS NULL OR reference = :reference) " +
            "ORDER BY date_created DESC",
            countQuery = "SELECT COUNT(*) FROM Transaction " +
                    "WHERE (:username IS NULL OR username = :username) " +
                    "AND (:reference IS NULL OR reference = :reference) ",
            nativeQuery = true)
    Page<Transaction> findAllBy(
            @Param("username") String username,
            @Param("reference") String reference,
            Pageable pageable
    );

    Page<Transaction> findAllByUsername(String username, Pageable page);
}
