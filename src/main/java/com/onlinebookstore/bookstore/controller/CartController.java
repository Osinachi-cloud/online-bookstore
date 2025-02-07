package com.onlinebookstore.bookstore.controller;

import com.onlinebookstore.bookstore.exceptions.BookStoreException;
import com.onlinebookstore.bookstore.model.dto.request.CartRequest;
import com.onlinebookstore.bookstore.model.dto.request.InitializeTransactionRequest;
import com.onlinebookstore.bookstore.model.dto.response.CartResponse;
import com.onlinebookstore.bookstore.model.dto.response.HttpResponse;
import com.onlinebookstore.bookstore.model.dto.response.InitializeTransactionResponse;
import com.onlinebookstore.bookstore.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * This service is for managing a user's cart, like adding and removing in the carts.
 * </p>
 *
 * <p>
 * Author: Ogbodo Uchenna
 * </p>
 * <p>
 * Version: 1.0
 * </p>
 * <p>
 * Date: 2025-02-05
 * </p>
 */

@RestController
@RequestMapping(value = "/api/v1/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Adds to cart.*
     * Passes {@link CartRequest} as a request body and the username as request parameter.*
     * @return the {@link CartResponse}, book object you have selected and it's quantity.
     */
    @PostMapping("/add")
    public ResponseEntity<HttpResponse<CartResponse>> addToCart(@RequestParam String username, @RequestBody CartRequest cartItemRequest) {
        return ResponseEntity.ok(cartService.addToCart(cartItemRequest, username));
    }

    /**
     * Removes from cart.*
     * Passes {@link CartRequest} as a request body and the username as request parameter.*
     * @return a {@link CartResponse}, book object you have selected and it's quantity.
     */
    @PostMapping("/remove")
    public ResponseEntity<HttpResponse<CartResponse>> removeFromCart(@RequestParam String username, @RequestBody CartRequest cartItemRequest) {
        return ResponseEntity.ok(cartService.removeFromCart(cartItemRequest, username));
    }

    /**
     * Gets all books in the cart for a user.*
     * Passes {@link CartRequest} as a request body and the username as request parameter.*
     * @return {@link CartResponse}, a list of books you have selected and their quantities.
     */
    @GetMapping("")
    public ResponseEntity<HttpResponse<List<CartResponse>>> getCartByUser(@RequestParam String username) {
        return ResponseEntity.ok(cartService.getCartByUser(username));
    }
}
