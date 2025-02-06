package com.onlinebookstore.bookstore.service.serviceImpl;

import com.onlinebookstore.bookstore.model.dto.request.InitializeTransactionRequest;
import com.onlinebookstore.bookstore.model.dto.request.Order;
import com.onlinebookstore.bookstore.model.entity.Book;
import com.onlinebookstore.bookstore.model.entity.BookOrder;
import com.onlinebookstore.bookstore.model.entity.Cart;
import com.onlinebookstore.bookstore.model.entity.User;
import com.onlinebookstore.bookstore.model.enums.Genre;
import com.onlinebookstore.bookstore.model.enums.OrderStatus;
import com.onlinebookstore.bookstore.model.enums.PaymentMethod;
import com.onlinebookstore.bookstore.repository.CartRepository;
import com.onlinebookstore.bookstore.repository.OrderRepository;
import com.onlinebookstore.bookstore.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private CartRepository mockCartRepository;
    @Mock
    private OrderRepository mockOrderRepository;
    @Mock
    private UserService mockUserService;

    private OrderServiceImpl orderServiceImplUnderTest;

    @BeforeEach
    void setUp() {
        orderServiceImplUnderTest = new OrderServiceImpl(mockCartRepository, mockOrderRepository, mockUserService);
    }

    @Test
    void testInitializeOrder() {
        final InitializeTransactionRequest request = new InitializeTransactionRequest();
        request.setUsername("username");
        request.setPaymentMethod(PaymentMethod.WEB);

        final Order expectedResult = new Order();
        expectedResult.setUsername("username");
        expectedResult.setTransactionId("REF5898a2c3dde547f0940bd350f344c6d3");

        final BookOrder bookOrder = new BookOrder();
        bookOrder.setOrderDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        bookOrder.setPaymentMethod(PaymentMethod.WEB);
        bookOrder.setOrderId("orderId");
        bookOrder.setTransactionId("REF5898a2c3dde547f0940bd350f344c6d3");

        final Book book = new Book();
        book.setTitle("title");
        bookOrder.setBook(book);
        bookOrder.setQuantity(0);
        final User user = new User();
        user.setUsername("username");
        bookOrder.setUser (user);
        bookOrder.setAmount(0.0);

        expectedResult.setOrderList(List.of(bookOrder));
        expectedResult.setOrderTotal(0.0);

        final User user1 = new User();
        user1.setUsername("username");
        when(mockUserService.getUser ("username")).thenReturn(user1);

        final Cart cart = new Cart();
        final Book book1 = new Book();
        book1.setTitle("title");
        cart.setBook(book1);
        cart.setQuantity(0);
        final User user2 = new User();
        user2.setUsername("username");
        cart.setUser (user2);
        cart.setAmount(0.0);

        final List<Cart> cartList = List.of(cart);
        when(mockCartRepository.findByUser (user2)).thenReturn(cartList);

        final BookOrder entity = new BookOrder();
        entity.setOrderDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        entity.setPaymentMethod(PaymentMethod.WEB);
        entity.setOrderId("orderId");
        entity.setTransactionId("REF5898a2c3dde547f0940bd350f344c6d3");

        final Book book3 = new Book();
        book3.setTitle("title");
        entity.setBook(book3);
        entity.setQuantity(0);
        final User user5 = new User();
        user5.setUsername("username");
        entity.setUser (user5);
        entity.setAmount(0.0);

        when(mockOrderRepository.save(any(BookOrder.class))).thenReturn(bookOrder);

        final Order result = orderServiceImplUnderTest.initializeOrder(request);

        assertThat(result.getUsername()).isEqualTo(expectedResult.getUsername());
        assertThat(result.getOrderList()).isEqualTo(expectedResult.getOrderList());
        assertThat(result.getTransactionId()).isNotNull();
    }


    @Test
    void testSave() {
        final BookOrder bookOrder = new BookOrder();
        bookOrder.setOrderDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        bookOrder.setPaymentMethod(PaymentMethod.WEB);
        bookOrder.setOrderId("orderId");
        bookOrder.setTransactionId("transactionId");
        final Book book = new Book();
        book.setPrice(100.00);
        bookOrder.setBook(book);
        bookOrder.setQuantity(2);
        final User user = new User();
        bookOrder.setUser(user);
        bookOrder.setAmount(200.0);

        final BookOrder expectedResult = new BookOrder();
        expectedResult.setOrderDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        expectedResult.setPaymentMethod(PaymentMethod.WEB);
        expectedResult.setOrderId("orderId");
        expectedResult.setTransactionId("transactionId");
        final Book book1 = new Book();
        book1.setPrice(100.00);
        expectedResult.setBook(book1);
        expectedResult.setQuantity(2);
        final User user1 = new User();
        expectedResult.setUser(user1);
        expectedResult.setAmount(200.0);

        final BookOrder bookOrder1 = new BookOrder();
        bookOrder1.setOrderDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        bookOrder1.setPaymentMethod(PaymentMethod.WEB);
        bookOrder1.setOrderId("orderId");
        bookOrder1.setTransactionId("transactionId");
        final Book book2 = new Book();
        book2.setPrice(100.00);
        bookOrder1.setBook(book2);
        bookOrder1.setQuantity(2);
        final User user2 = new User();
        bookOrder1.setUser(user2);
        bookOrder1.setAmount(200.0);
        final BookOrder entity = new BookOrder();
        entity.setOrderDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        entity.setPaymentMethod(PaymentMethod.WEB);
        entity.setOrderId("orderId");
        entity.setTransactionId("transactionId");
        final Book book3 = new Book();
        book3.setPrice(100.00);
        entity.setBook(book3);
        entity.setQuantity(2);
        final User user3 = new User();
        entity.setUser(user3);
        entity.setAmount(200.0);
        when(mockOrderRepository.save(entity)).thenReturn(bookOrder1);

        final BookOrder result = orderServiceImplUnderTest.save(bookOrder);

        assertThat(result).isEqualTo(expectedResult);
    }


    @Test
    void testGetOrdersByTransactionId() {
        final BookOrder bookOrder = new BookOrder();
        bookOrder.setOrderDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        bookOrder.setPaymentMethod(PaymentMethod.WEB);
        bookOrder.setPaid(false);
        bookOrder.setOrderId("orderId");
        bookOrder.setTransactionId("transactionId");

        final Book book = new Book();
        book.setTitle("title");
        book.setGenre(Genre.FICTION);
        book.setIsbn("012345");
        book.setAuthor("Harry");
        book.setYearOfPublication("1960");
        book.setPrice(100.00);
        bookOrder.setBook(book);

        bookOrder.setQuantity(1);
        final User user = new User();
        user.setUsername("username");
        bookOrder.setUser (user);
        bookOrder.setAmount(100.0);
        bookOrder.setStatus(OrderStatus.COMPLETED);

        final List<BookOrder> expectedResult = List.of(bookOrder);

        final BookOrder bookOrder1 = new BookOrder();
        bookOrder1.setOrderDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        bookOrder1.setPaymentMethod(PaymentMethod.WEB);
        bookOrder1.setPaid(false);
        bookOrder1.setOrderId("orderId");
        bookOrder1.setTransactionId("transactionId");

        final Book book1 = new Book();
        book1.setTitle("title");
        book1.setGenre(Genre.FICTION);
        book1.setIsbn("012345");
        book1.setAuthor("Harry");
        book1.setYearOfPublication("1960");
        book1.setPrice(100.00);
        bookOrder1.setBook(book1);

        bookOrder1.setQuantity(1);
        final User user1 = new User();
        user1.setUsername("username");
        bookOrder1.setUser (user1);
        bookOrder1.setAmount(100.0);
        bookOrder1.setStatus(OrderStatus.COMPLETED);

        final List<BookOrder> bookOrderList = List.of(bookOrder1);
        when(mockOrderRepository.findOrdersByTransactionId("orderId")).thenReturn(bookOrderList);

        final List<BookOrder> result = orderServiceImplUnderTest.getOrdersByTransactionId("orderId");

        assertThat(result).isEqualTo(expectedResult);
    }

}
