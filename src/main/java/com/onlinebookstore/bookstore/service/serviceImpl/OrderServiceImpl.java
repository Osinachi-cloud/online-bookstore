package com.onlinebookstore.bookstore.service.serviceImpl;

import com.onlinebookstore.bookstore.exceptions.BookStoreException;
import com.onlinebookstore.bookstore.model.dto.request.InitializeTransactionRequest;
import com.onlinebookstore.bookstore.model.dto.request.Order;
import com.onlinebookstore.bookstore.model.entity.Book;
import com.onlinebookstore.bookstore.model.entity.Cart;
import com.onlinebookstore.bookstore.model.entity.BookOrder;
import com.onlinebookstore.bookstore.model.entity.User;
import com.onlinebookstore.bookstore.model.enums.PaymentMethod;
import com.onlinebookstore.bookstore.repository.CartRepository;
import com.onlinebookstore.bookstore.repository.OrderRepository;
import com.onlinebookstore.bookstore.service.OrderService;
import com.onlinebookstore.bookstore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.onlinebookstore.bookstore.utils.CommonUtils.generateRandomUUIDString;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    private final UserService userService;

    public OrderServiceImpl(CartRepository cartRepository, OrderRepository orderRepository, UserService userService) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public Order initializeOrder(InitializeTransactionRequest request) {

        if(!verifyPaymentMethod(request)){
            throw new BookStoreException("Invalid Payment method");
        }
        User cartOwner = userService.getUser(request.getUsername());

        List<Cart> cartList = cartRepository.findByUser(cartOwner);
        List<BookOrder> bookOrderList = new ArrayList<>();
        String transId = "REF" +generateRandomUUIDString();
        double amount = 0;

         for(Cart cart :cartList){
            Book book = cart.getBook();
            User user = cart.getUser();

            BookOrder bookOrder = new BookOrder();
            bookOrder.setOrderDate(LocalDateTime.now());
            bookOrder.setOrderId(generateRandomUUIDString());
            bookOrder.setTransactionId(transId);
            bookOrder.setBook(book);
            bookOrder.setUser(user);
            bookOrder.setAmount(cart.getAmount());
            bookOrder.setQuantity(cart.getQuantity());
            bookOrder.setPaymentMethod(request.getPaymentMethod());
            amount = amount + cart.getAmount();

            BookOrder savedBookOrder = orderRepository.save(bookOrder);
            bookOrderList.add(savedBookOrder);
        }

         cartRepository.deleteByUser(cartOwner);

        Order order = new Order();
         order.setUsername(request.getUsername());
        order.setTransactionId(transId);
        order.setOrderList(bookOrderList);
        order.setOrderTotal(amount);
        return order;
    }

    @Override
    public BookOrder save(BookOrder bookOrder) {
        return orderRepository.save(bookOrder);
    }

    @Override
    public List<BookOrder> getOrdersByTransactionId(String orderId) {
        return orderRepository.findOrdersByTransactionId(orderId);
    }

    public boolean verifyPaymentMethod(InitializeTransactionRequest orderRequest) {
        return orderRequest.getPaymentMethod() != null && orderRequest.getPaymentMethod().equals(PaymentMethod.TRANSFER)
                || Objects.equals(orderRequest.getPaymentMethod(), PaymentMethod.WEB)
                || Objects.equals(orderRequest.getPaymentMethod(), PaymentMethod.USSD);
    }

}
