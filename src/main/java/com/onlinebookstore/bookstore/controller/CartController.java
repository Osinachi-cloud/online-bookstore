package com.onlinebookstore.bookstore.controller;

import com.onlinebookstore.bookstore.model.dto.request.CartRequest;
import com.onlinebookstore.bookstore.model.dto.response.CartResponse;
import com.onlinebookstore.bookstore.model.dto.response.HttpResponse;
import com.onlinebookstore.bookstore.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<HttpResponse<CartResponse>> addToCart(@RequestBody CartRequest cartItemRequest) {
        return ResponseEntity.ok(cartService.addToCart(cartItemRequest));
    }

    @PostMapping("/remove")
    public ResponseEntity<HttpResponse<CartResponse>> removeFromCart(@RequestBody CartRequest cartItemRequest) {
        return ResponseEntity.ok(cartService.removeFromCart(cartItemRequest));
    }

    @GetMapping("")
    public ResponseEntity<HttpResponse<List<CartResponse>>> getCartByUser(@RequestParam String username) {
        return ResponseEntity.ok(cartService.getCartByUser(username));
    }
}
