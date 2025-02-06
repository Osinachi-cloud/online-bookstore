package com.onlinebookstore.bookstore.service;

import com.onlinebookstore.bookstore.model.dto.request.CartRequest;
import com.onlinebookstore.bookstore.model.dto.response.CartResponse;
import com.onlinebookstore.bookstore.model.dto.response.HttpResponse;

import java.util.List;

public interface CartService {

    HttpResponse<CartResponse> addToCart(CartRequest cartRequest, String username);
    HttpResponse<List<CartResponse>> getCartByUser(String username);

    HttpResponse<CartResponse> removeFromCart(CartRequest cartItemRequest, String username);
}
