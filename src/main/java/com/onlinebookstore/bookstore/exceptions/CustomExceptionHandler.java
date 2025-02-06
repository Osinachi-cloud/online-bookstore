package com.onlinebookstore.bookstore.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@RestControllerAdvice
@Slf4j

public class CustomExceptionHandler{

    private static final String API_UNHANDLED_EXCEPTION = "error: %s";
    private static final HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;

    private static final HttpStatus methodNotAllowedError = HttpStatus.METHOD_NOT_ALLOWED;

    private static final HttpStatus badRequestError = HttpStatus.BAD_REQUEST;

    private static final HttpStatus accessDeniedError = HttpStatus.FORBIDDEN;


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(Exception e) {
        ApiException exceptionDetail = new ApiException(
                String.format(API_UNHANDLED_EXCEPTION, e.getMessage()),
                internalServerError,
                ZonedDateTime.now(ZoneId.systemDefault()));
        return new ResponseEntity<Object>(exceptionDetail, internalServerError);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleMethodNotAllowed(HttpRequestMethodNotSupportedException e) {
        ApiException exceptionDetail = new ApiException(
                "Method not allowed: " + e.getMethod(),
                methodNotAllowedError,
                ZonedDateTime.now(ZoneId.systemDefault()));
        return new ResponseEntity<>(exceptionDetail, methodNotAllowedError);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiException exceptionDetail = new ApiException(
                String.format(API_UNHANDLED_EXCEPTION, ex.getMessage()),
                internalServerError,
                ZonedDateTime.now(ZoneId.systemDefault()));
        return new ResponseEntity<Object>(exceptionDetail, internalServerError);
    }

    @ExceptionHandler(BookStoreException.class)
    public ResponseEntity<Object> handleApiRequestException(BookStoreException e){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                e.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> validationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        var fieldErrorList = (FieldError[])(fieldErrors.toArray(new FieldError[fieldErrors.size()]));

        ApiException exceptionDetail = new ApiException(fieldErrorList[0].getDefaultMessage(),HttpStatus.BAD_REQUEST, ZonedDateTime.now());
        return new ResponseEntity<>(exceptionDetail, badRequestError);
    }

}