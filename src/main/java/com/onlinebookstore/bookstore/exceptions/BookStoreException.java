package com.onlinebookstore.bookstore.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BookStoreException extends RuntimeException {
    private HttpStatus httpStatus;
    public BookStoreException(String message){
        super(message);
    }
    public BookStoreException(String message, HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }

    public BookStoreException(String message, Throwable cause){
        super(message, cause);
    }

}
