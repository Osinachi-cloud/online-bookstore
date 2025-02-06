package com.onlinebookstore.bookstore.service.serviceImpl;

import com.onlinebookstore.bookstore.exceptions.BookStoreException;
import com.onlinebookstore.bookstore.model.dto.response.HttpResponse;
import com.onlinebookstore.bookstore.model.dto.response.TransactionResponse;
import com.onlinebookstore.bookstore.model.entity.Book;
import com.onlinebookstore.bookstore.model.entity.BookOrder;
import com.onlinebookstore.bookstore.model.entity.Transaction;
import com.onlinebookstore.bookstore.model.entity.User;
import com.onlinebookstore.bookstore.repository.TransactionRepository;
import com.onlinebookstore.bookstore.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testSaveTransaction() {
        Transaction transaction = new Transaction();
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction savedTransaction = transactionService.saveTransaction(transaction);

        assertNotNull(savedTransaction);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    public void testFindByReference_Success() {
        String reference = "TRANSACTION_ID";
        Transaction transaction = new Transaction();
        when(transactionRepository.findByReference(reference)).thenReturn(Optional.of(transaction));

        Transaction foundTransaction = transactionService.findByReference(reference);

        assertNotNull(foundTransaction);
        verify(transactionRepository, times(1)).findByReference(reference);
    }

    @Test
    public void testFindByReference_NotFound() {
        String reference = "INVALID_TRANSACTION_ID";
        when(transactionRepository.findByReference(reference)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BookStoreException.class, () -> {
            transactionService.findByReference(reference);
        });
        assertEquals("Transaction does not exist", exception.getMessage());
    }

    @Test
    public void testExistsByReference_Exists() {
        String reference = "TRANSACTION_ID";
        when(transactionRepository.findByReference(reference)).thenReturn(Optional.of(new Transaction()));

        boolean exists = transactionService.existsByReference(reference);

        assertTrue(exists);
        verify(transactionRepository, times(1)).findByReference(reference);
    }

    @Test
    public void testExistsByReference_NotExists() {
        String reference = "INVALID_TRANSACTION_ID";
        when(transactionRepository.findByReference(reference)).thenReturn(Optional.empty());

        boolean exists = transactionService.existsByReference(reference);

        assertFalse(exists);
        verify(transactionRepository, times(1)).findByReference(reference);
    }

    @Test
    public void testGetTransactionBy_WithReference() {
        String username = "testUser";
        String reference = "TRANSACTION_ID";
        int pageNum = 0;
        int pageSize = 10;

        User user = new User();
        user.setUsername(username);

        Book book = new Book();
        book.setAuthor("author");

        Transaction transaction = new Transaction();
        transaction.setTransactionId(reference);
        transaction.setUsername(username);

        when(transactionRepository.findAllBy(username, reference, PageRequest.of(pageNum, pageSize)))
                .thenReturn(new PageImpl<>(List.of(transaction)));

        BookOrder bookOrder = new BookOrder();
        bookOrder.setTransactionId(reference);
        bookOrder.setUser(user);
        bookOrder.setBook(book);

        when(orderService.getOrdersByTransactionId(reference)).thenReturn(List.of(bookOrder));

        HttpResponse<Page<TransactionResponse>> response = transactionService.getTransactionBy(username, reference, pageNum, pageSize);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("purchase history", response.getMessage());
        assertEquals(1, response.getData().getTotalElements());
    }

    @Test
    public void testGetTransactionBy_WithoutReference() {
        String username = "testUser";
        int pageNum = 0;
        int pageSize = 10;

        User user = new User();
        user.setUsername(username);

        Book book = new Book();
        book.setAuthor("author");

        Transaction transaction = new Transaction();
        transaction.setTransactionId("TRANSACTION_ID");
        transaction.setUsername(username);

        when(transactionRepository.findAllByUsername(username, PageRequest.of(pageNum, pageSize)))
                .thenReturn(new PageImpl<>(List.of(transaction)));

        BookOrder bookOrder = new BookOrder();
        bookOrder.setTransactionId(transaction.getTransactionId());
        bookOrder.setUser(user);
        bookOrder.setBook(book);

        when(orderService.getOrdersByTransactionId(transaction.getTransactionId())).thenReturn(List.of(bookOrder));

        HttpResponse<Page<TransactionResponse>> response = transactionService.getTransactionBy(username, null, pageNum, pageSize);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("purchase history", response.getMessage());
        assertEquals(1, response.getData().getTotalElements());
    }
}