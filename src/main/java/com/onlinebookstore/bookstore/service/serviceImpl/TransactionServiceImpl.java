package com.onlinebookstore.bookstore.service.serviceImpl;


import com.onlinebookstore.bookstore.exceptions.BookStoreException;
import com.onlinebookstore.bookstore.model.dto.response.HttpResponse;
import com.onlinebookstore.bookstore.model.dto.response.TransactionResponse;
import com.onlinebookstore.bookstore.model.entity.BookOrder;
import com.onlinebookstore.bookstore.model.entity.Transaction;
import com.onlinebookstore.bookstore.repository.TransactionRepository;
import com.onlinebookstore.bookstore.service.OrderService;
import com.onlinebookstore.bookstore.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.onlinebookstore.bookstore.utils.ModelMapper.*;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final OrderService orderService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, OrderService orderService) {
        this.transactionRepository = transactionRepository;
        this.orderService = orderService;
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction findByReference(String reference) {

        Optional<Transaction> optionalTransaction = transactionRepository.findByReference(reference);
        if(optionalTransaction.isEmpty()){
            throw new BookStoreException("Transaction does not exist");
        }
        return optionalTransaction.get();
    }

    @Override
    public boolean existsByReference(String reference) {

        Optional<Transaction> optionalTransaction = transactionRepository.findByReference(reference);
        return optionalTransaction.isPresent();
    }

    @Override
    public HttpResponse<Page<TransactionResponse>> getTransactionBy(String username, String reference, int pageNum, int pageSize) {
        Pageable page = PageRequest.of(pageNum, pageSize);
        Page<Transaction> transactionPage;

        if (reference != null && !reference.isEmpty()) {
            transactionPage = transactionRepository.findAllBy(username, reference, page);
        } else {
            transactionPage = transactionRepository.findAllByUsername(username, page);
        }

        log.info("transactions :{}", transactionPage);
        List<Transaction> transactionList = transactionPage.getContent();
        List<TransactionResponse> transactionResponseList = new ArrayList<>();

        if (reference == null || reference.isEmpty()) {
            for (Transaction transaction : transactionList) {
                List<BookOrder> bookOrderList = orderService.getOrdersByTransactionId(transaction.getTransactionId());
                TransactionResponse transactionResponse = mapTransactionModelToResponse(transaction);
                transactionResponse.setBookOrderList(mapBookOrderListToResponse(bookOrderList));
                transactionResponseList.add(transactionResponse);
            }
        } else {
            List<BookOrder> bookOrderList = orderService.getOrdersByTransactionId(reference);
            transactionResponseList = mapTransactionListToResponse(transactionList, bookOrderList);
        }

        Page<TransactionResponse> transactionResponsePage = new PageImpl<>(transactionResponseList, page, transactionList.size());

        return HttpResponse.<Page<TransactionResponse>>builder()
                .data(transactionResponsePage)
                .statusCode(200)
                .message("purchase history")
                .status(HttpStatus.OK)
                .build();
    }
}
