package com.onlinebookstore.bookstore.service;

import com.onlinebookstore.bookstore.model.dto.request.UserRequest;
import com.onlinebookstore.bookstore.model.dto.response.HttpResponse;
import com.onlinebookstore.bookstore.model.dto.response.UserResponse;
import com.onlinebookstore.bookstore.model.entity.User;

public interface UserService {

    boolean userExists(String username);

    User getUser(String id);

    HttpResponse<UserResponse> createUser(UserRequest request);
}
