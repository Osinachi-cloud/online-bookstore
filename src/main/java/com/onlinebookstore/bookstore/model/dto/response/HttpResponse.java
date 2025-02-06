package com.onlinebookstore.bookstore.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@SuperBuilder
@JsonInclude(NON_DEFAULT)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class HttpResponse <T>{
    protected String timeStamp;
    protected int statusCode;
    protected HttpStatus status;
    protected String message;
    protected T data;
}