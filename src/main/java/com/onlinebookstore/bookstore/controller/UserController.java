package com.onlinebookstore.bookstore.controller;

import com.onlinebookstore.bookstore.model.dto.request.UserRequest;
import com.onlinebookstore.bookstore.model.dto.response.HttpResponse;
import com.onlinebookstore.bookstore.model.dto.response.UserResponse;
import com.onlinebookstore.bookstore.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create-user")
    public ResponseEntity<HttpResponse<UserResponse>> createUser(@RequestBody UserRequest request){
        return ResponseEntity.ok(userService.createUser(request));
    }

}
