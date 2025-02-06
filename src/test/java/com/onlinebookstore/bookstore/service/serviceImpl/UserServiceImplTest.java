package com.onlinebookstore.bookstore.service.serviceImpl;

import com.onlinebookstore.bookstore.exceptions.BookStoreException;
import com.onlinebookstore.bookstore.model.dto.request.UserRequest;
import com.onlinebookstore.bookstore.model.dto.response.HttpResponse;
import com.onlinebookstore.bookstore.model.dto.response.UserResponse;
import com.onlinebookstore.bookstore.model.entity.User;
import com.onlinebookstore.bookstore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testUserExists_UserExists() {
        String username = "testUser ";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        boolean exists = userService.userExists(username);

        assertTrue(exists);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testUserExists_UserDoesNotExist() {
        String username = "nonExistentUser ";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        boolean exists = userService.userExists(username);

        assertFalse(exists);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testGetUser_UserExists() {
        String username = "testUser ";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User foundUser  = userService.getUser (username);

        assertNotNull(foundUser );
        assertEquals(username, foundUser .getUsername());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testGetUser_UserDoesNotExist() {
        String username = "nonExistentUser ";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BookStoreException.class, () -> {
            userService.getUser (username);
        });
        assertEquals("User not present", exception.getMessage());
    }

    @Test
    public void testCreateUser_Success() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("newUser ");

        User user = new User();
        user.setUsername("newUser ");

        when(userRepository.save(any(User.class))).thenReturn(user);

        HttpResponse<UserResponse> response = userService.createUser (userRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertEquals("User successfully created", response.getMessage());
        assertEquals("newUser ", response.getData().getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }
}