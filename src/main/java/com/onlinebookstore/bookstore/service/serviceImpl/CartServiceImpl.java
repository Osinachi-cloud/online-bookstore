package com.onlinebookstore.bookstore.service.serviceImpl;

import com.onlinebookstore.bookstore.exceptions.BookStoreException;
import com.onlinebookstore.bookstore.model.dto.request.CartRequest;
import com.onlinebookstore.bookstore.model.dto.response.CartResponse;
import com.onlinebookstore.bookstore.model.dto.response.HttpResponse;
import com.onlinebookstore.bookstore.model.entity.Book;
import com.onlinebookstore.bookstore.model.entity.Cart;
import com.onlinebookstore.bookstore.model.entity.User;
import com.onlinebookstore.bookstore.repository.CartRepository;
import com.onlinebookstore.bookstore.service.BookService;
import com.onlinebookstore.bookstore.service.CartService;
import com.onlinebookstore.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.onlinebookstore.bookstore.utils.ModelMapper.mapCartListToResponse;
import static com.onlinebookstore.bookstore.utils.ModelMapper.mapCartModelToResponse;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final BookService bookService;
    private final UserService userService;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, BookService bookService, UserService userService) {
        this.cartRepository = cartRepository;
        this.bookService = bookService;
        this.userService = userService;
    }

    @Override
    public HttpResponse<CartResponse> addToCart(CartRequest cartRequest, String username){
        Book book = bookService.getBook(cartRequest.getBookId());
        User user = userService.getUser(username);

        Optional<Cart> existingCart = cartRepository.findByUserAndBook(user, book);
        if(existingCart.isEmpty()){
            Cart cart = new Cart();
            cart.setBook(book);
            cart.setQuantity(cartRequest.getQuantity());
            cart.setUser(user);
            cart.setAmount(book.getPrice() * cartRequest.getQuantity());
            Cart savedCart = cartRepository.save(cart);
            return HttpResponse.<CartResponse>builder()
                    .message("successful")
                    .statusCode(200)
                    .status(HttpStatus.OK)
                    .data(mapCartModelToResponse(savedCart))
                    .build();
        } else {
            Cart cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + cartRequest.getQuantity());
            cart.setAmount(book.getPrice() * cart.getQuantity());
            Cart savedCart = cartRepository.save(cart);
            return HttpResponse.<CartResponse>builder()
                    .message("successful")
                    .statusCode(200)
                    .status(HttpStatus.OK)
                    .data(mapCartModelToResponse(savedCart))
                    .build();
        }
    }

    @Override
    public HttpResponse<List<CartResponse>> getCartByUser(String username) {
        User user = userService.getUser(username);
        List<Cart> cart = cartRepository.findByUser(user);

        return HttpResponse.<List<CartResponse>>builder()
                .data(mapCartListToResponse(cart))
                .message("success")
                .status(HttpStatus.OK)
                .statusCode(200)
                .build();
    }

    @Override
    public HttpResponse<CartResponse> removeFromCart(CartRequest cartRequest, String username) {
        Book book = bookService.getBook(cartRequest.getBookId());
        User user = userService.getUser(username);

        Optional<Cart> existingCart = cartRepository.findByUserAndBook(user, book);

        if(existingCart.isEmpty()){
            throw new BookStoreException("Book does not exist in cart");
        } else if(existingCart.get().getQuantity() <= 1) {
            cartRepository.delete(existingCart.get());
            return HttpResponse.<CartResponse>builder()
                    .message("removed from cart")
                    .statusCode(204)
                    .status(HttpStatus.OK)
                    .data(null)
                    .build();
        } else {
            Cart cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() - 1);
            cart.setAmount(book.getPrice() * cart.getQuantity());
            Cart savedCart = cartRepository.save(cart);
            return HttpResponse.<CartResponse>builder()
                    .message("successful")
                    .statusCode(200)
                    .status(HttpStatus.OK)
                    .data(mapCartModelToResponse(savedCart))
                    .build();
        }
    }


}
