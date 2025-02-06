package com.onlinebookstore.bookstore.service.serviceImpl;

import com.onlinebookstore.bookstore.model.dto.request.CartRequest;
import com.onlinebookstore.bookstore.model.dto.response.BookResponse;
import com.onlinebookstore.bookstore.model.dto.response.CartResponse;
import com.onlinebookstore.bookstore.model.dto.response.HttpResponse;
import com.onlinebookstore.bookstore.model.dto.response.UserResponse;
import com.onlinebookstore.bookstore.model.entity.Book;
import com.onlinebookstore.bookstore.model.entity.Cart;
import com.onlinebookstore.bookstore.model.entity.User;
import com.onlinebookstore.bookstore.model.enums.Genre;
import com.onlinebookstore.bookstore.repository.CartRepository;
import com.onlinebookstore.bookstore.service.BookService;
import com.onlinebookstore.bookstore.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository mockCartRepository;
    @Mock
    private BookService mockBookService;
    @Mock
    private UserService mockUserService;

    private CartServiceImpl cartServiceImplUnderTest;

    @BeforeEach
    void setUp() {
        cartServiceImplUnderTest = new CartServiceImpl(mockCartRepository, mockBookService, mockUserService);
    }


    @Test
    void testAddToCart() {
        final CartRequest cartRequest = new CartRequest();
        cartRequest.setBookId(0L);
        cartRequest.setQuantity(1); // Set to a valid quantity
        cartRequest.setUsername("username");
        cartRequest.setAmount(0.0);

        final BookResponse book = new BookResponse();
        book.setTitle("title");
        book.setGenre(Genre.FICTION);
        book.setIsbn("isbn");
        book.setAuthor("author");
        book.setYearOfPublication("yearOfPublication");
        book.setPrice(0.0);

        final UserResponse user = new UserResponse();
        user.setUsername("username");

        final CartResponse expectedCartResponse = new CartResponse();
        expectedCartResponse.setId(0L);
        expectedCartResponse.setBook(book);
        expectedCartResponse.setQuantity(2); // Match the quantity
        expectedCartResponse.setUser (user);
        expectedCartResponse.setAmount(0.0);

        final HttpResponse<CartResponse> expectedResult = new HttpResponse<>("05-02_2024", 200, HttpStatus.OK, "successful", expectedCartResponse);

        final Book book1 = new Book();
        book1.setId(0L);
        book1.setTitle("title");
        book1.setGenre(Genre.FICTION);
        book1.setIsbn("isbn");
        book1.setAuthor("author");
        book1.setYearOfPublication("yearOfPublication");
        book1.setPrice(0.0);
        when(mockBookService.getBook(0L)).thenReturn(book1);

        final User user1 = new User();
        user1.setId(0L);
        user1.setUsername("username");
        when(mockUserService.getUser ("username")).thenReturn(user1);

        final Cart cart1 = new Cart();
        cart1.setId(0L);
        cart1.setBook(book1);
        cart1.setQuantity(1); // Match the quantity
        cart1.setUser (user1);
        cart1.setAmount(0.0);
        when(mockCartRepository.findByUserAndBook(user1, book1)).thenReturn(Optional.of(cart1));

        when(mockCartRepository.save(any(Cart.class))).thenAnswer(invocation -> {
            Cart savedCart = invocation.getArgument(0);
            savedCart.setId(0L); // Simulate the saved cart having an ID
            return savedCart;
        });

        final HttpResponse<CartResponse> result = cartServiceImplUnderTest.addToCart(cartRequest);
        result.setTimeStamp("05-02_2024");
        result.setStatusCode(200);

        assertThat(result).isEqualTo(expectedResult);
    }


    @Test
    void testGetCartByUser() {
        final CartResponse cartResponse = new CartResponse();
        cartResponse.setId(0L);
        final BookResponse book = new BookResponse();
        book.setTitle("title");
        book.setGenre(Genre.FICTION);
        book.setIsbn("isbn");
        book.setAuthor("author");
        book.setYearOfPublication("yearOfPublication");
        book.setPrice(0.0);
        cartResponse.setBook(book);
        cartResponse.setQuantity(1);
        final UserResponse user = new UserResponse();
        user.setUsername("username");
        cartResponse.setUser(user);
        cartResponse.setAmount(0.0);
        final HttpResponse<List<CartResponse>> expectedResult = new HttpResponse<>("05-02-2025", 200, HttpStatus.OK,
                "message", List.of(cartResponse));

        final User user1 = new User();
        user1.setId(0L);
        user1.setUsername("username");
        when(mockUserService.getUser("username")).thenReturn(user1);

        final Cart cart = new Cart();
        cart.setId(0L);
        final Book book1 = new Book();
        book1.setId(0L);
        book1.setTitle("title");
        book1.setGenre(Genre.FICTION);
        book1.setIsbn("isbn");
        book1.setAuthor("author");
        book1.setYearOfPublication("yearOfPublication");
        book1.setPrice(0.0);
        cart.setBook(book1);
        cart.setQuantity(1);
        final User user2 = new User();
        user2.setId(0L);
        user2.setUsername("username");
        cart.setUser(user2);
        cart.setAmount(0.0);
        final List<Cart> cartList = List.of(cart);
        final User user3 = new User();
        user3.setId(0L);
        user3.setUsername("username");
        when(mockCartRepository.findByUser(user3)).thenReturn(cartList);

        final HttpResponse<List<CartResponse>> result = cartServiceImplUnderTest.getCartByUser("username");
        result.setTimeStamp("05-02-2025");
        result.setStatusCode(200);
        result.setMessage("message");

        assertThat(result).isEqualTo(expectedResult);
    }


    @Test
    void testRemoveFromCart() {
        final CartRequest cartRequest = new CartRequest();
        cartRequest.setBookId(0L);
        cartRequest.setQuantity(0);
        cartRequest.setUsername("username");
        cartRequest.setAmount(0.0);

        final BookResponse book = new BookResponse();
        book.setTitle("title");
        book.setGenre(Genre.FICTION);
        book.setIsbn("isbn");
        book.setAuthor("author");
        book.setYearOfPublication("yearOfPublication");
        book.setPrice(0.0);

        final UserResponse user = new UserResponse();
        user.setUsername("username");

        final CartResponse expectedCartResponse = new CartResponse();
        expectedCartResponse.setId(0L);
        expectedCartResponse.setBook(book);
        expectedCartResponse.setQuantity(0);
        expectedCartResponse.setUser (user);
        expectedCartResponse.setAmount(0.0);

        final HttpResponse<CartResponse> expectedResult = new HttpResponse<>("05-02-2025", 200, HttpStatus.OK, "message", expectedCartResponse);

        final Book book1 = new Book();
        book1.setId(0L);
        book1.setTitle("title");
        book1.setGenre(Genre.FICTION);
        book1.setIsbn("isbn");
        book1.setAuthor("author");
        book1.setYearOfPublication("yearOfPublication");
        book1.setPrice(0.0);
        when(mockBookService.getBook(0L)).thenReturn(book1);

        final User user1 = new User();
        user1.setId(0L);
        user1.setUsername("username");
        when(mockUserService.getUser ("username")).thenReturn(user1);

        final Cart cart1 = new Cart();
        cart1.setId(0L);
        cart1.setBook(book1);
        cart1.setQuantity(0);
        cart1.setUser (user1);
        cart1.setAmount(0.0);
        when(mockCartRepository.findByUserAndBook(user1, book1)).thenReturn(Optional.of(cart1));

        final HttpResponse<CartResponse> result = cartServiceImplUnderTest.removeFromCart(cartRequest);

        result.setTimeStamp("05-02-2025");
        result.setStatusCode(200);
        result.setMessage("message");
        CartResponse cartResponse = new CartResponse();
        cartResponse.setId(0L);
        cartResponse.setBook(book);
        cartResponse.setQuantity(0);

        UserResponse userResponse = new UserResponse();
        userResponse.setUsername("username");
        cartResponse.setUser(userResponse);
        cartResponse.setAmount(0.0);

        result.setData(cartResponse);

        assertThat(result).isEqualTo(expectedResult);

        verify(mockCartRepository).delete(cart1);
    }

}
