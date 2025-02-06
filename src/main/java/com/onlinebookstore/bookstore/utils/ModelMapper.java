package com.onlinebookstore.bookstore.utils;

import com.onlinebookstore.bookstore.model.dto.request.BookRequest;
import com.onlinebookstore.bookstore.model.dto.request.UserRequest;
import com.onlinebookstore.bookstore.model.dto.response.*;
import com.onlinebookstore.bookstore.model.entity.*;

import java.util.ArrayList;
import java.util.List;

public class ModelMapper {

    public static User mapUserRequestToModel(UserRequest userRequest){
        User user = new User();
        user.setUsername(userRequest.getUsername());
        return user;
    }
    public static UserResponse mapUserModelToResponse(User user){
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        return userResponse;
    }
    public static BookResponse mapBookModelToResponse(Book book){
        BookResponse bookResponse = new BookResponse();
        bookResponse.setAuthor(book.getAuthor());
        bookResponse.setIsbn(book.getIsbn());
        bookResponse.setGenre(book.getGenre());
        bookResponse.setTitle(book.getTitle());
        bookResponse.setPrice(book.getPrice());
        bookResponse.setYearOfPublication(book.getYearOfPublication());

        return bookResponse;
    }

    public static UserResponse mapBookModelToResponse(User user){
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        return userResponse;
    }

    public static Book mapBookRequestToEntity(BookRequest bookRequest){
        Book book = new Book();
        book.setAuthor(bookRequest.getAuthor());
        book.setIsbn(bookRequest.getIsbn());
        book.setGenre(bookRequest.getGenre());
        book.setTitle(bookRequest.getTitle());
        book.setYearOfPublication(bookRequest.getYearOfPublication());
        book.setPrice(bookRequest.getPrice());

        return book;
    }

    public static List<BookResponse> mapListOfBooksToBookResponse(List<Book> bookList){
         return bookList.stream().map(ModelMapper::mapBookModelToResponse).toList();
    }

    public static CartResponse mapCartModelToResponse(Cart cart){
        CartResponse cartResponse = new CartResponse();
        cartResponse.setId(cart.getId());
        cartResponse.setBook(mapBookModelToResponse(cart.getBook()));
        cartResponse.setQuantity(cart.getQuantity());
        cartResponse.setUser(mapBookModelToResponse(cart.getUser()));
        cartResponse.setAmount(cart.getAmount());

        return cartResponse;
    }

    public static List<CartResponse> mapCartListToResponse(List<Cart> cart){
        return cart.stream().map(ModelMapper::mapCartModelToResponse).toList();
    }


    public static TransactionResponse mapTransactionModelToResponse(Transaction transaction){
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setTransactionId(transaction.getTransactionId());
        transactionResponse.setPaymentMethod(transaction.getPaymentMethod());
        transactionResponse.setUsername(transaction.getUsername());
        transactionResponse.setReference(transaction.getReference());
        transactionResponse.setAmount(transaction.getAmount());
        transactionResponse.setPaidAt(transaction.getPaidAt());
        transactionResponse.setStatus(transaction.getStatus());

        return transactionResponse;
    }
    public static BookOrderResponse mapBookOrderModelToResponse(BookOrder bookOrder){
        BookOrderResponse bookOrderResponse = new BookOrderResponse();
        bookOrderResponse.setUser(mapUserModelToResponse(bookOrder.getUser()));
        bookOrderResponse.setBookResponse(mapBookModelToResponse(bookOrder.getBook()));
        bookOrderResponse.setOrderId(bookOrder.getOrderId());
        bookOrderResponse.setQuantity(bookOrder.getQuantity());
        bookOrderResponse.setAmount(bookOrder.getAmount());
        bookOrderResponse.setStatus(bookOrder.getStatus());

        return bookOrderResponse;
    }

    public static List<BookOrderResponse> mapBookOrderListToResponse(List<BookOrder> bookOrderList){
        return bookOrderList.stream().map(ModelMapper::mapBookOrderModelToResponse).toList();
    }
    public static List<TransactionResponse> mapTransactionListToResponse(List<Transaction> transactions, List<BookOrder> bookOrderList){
        List<TransactionResponse> transactionResponseList = new ArrayList<>();
        for(Transaction transaction: transactions){
            TransactionResponse transactionResponse = mapTransactionModelToResponse(transaction);
            transactionResponse.setBookOrderList(mapBookOrderListToResponse(bookOrderList));
            transactionResponseList.add(transactionResponse);
        }

        return transactionResponseList;
    }

}
