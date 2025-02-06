package com.onlinebookstore.bookstore.service.serviceImpl;

import com.onlinebookstore.bookstore.exceptions.BookStoreException;
import com.onlinebookstore.bookstore.model.dto.request.UserRequest;
import com.onlinebookstore.bookstore.model.dto.response.HttpResponse;
import com.onlinebookstore.bookstore.model.dto.response.UserResponse;
import com.onlinebookstore.bookstore.model.entity.User;
import com.onlinebookstore.bookstore.repository.UserRepository;
import com.onlinebookstore.bookstore.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import static com.onlinebookstore.bookstore.utils.ModelMapper.mapUserModelToResponse;
import static com.onlinebookstore.bookstore.utils.ModelMapper.mapUserRequestToModel;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean userExists(String username){
        Optional<User> userExists = userRepository.findByUsername(username);
        return userExists.isPresent();
    }

    @Override
    public User getUser(String username){
        Optional<User> userExists = userRepository.findByUsername(username);
        if(userExists.isEmpty()){
            throw new BookStoreException("User not present");
        }
        return userExists.get();
    }

    @Override
    public HttpResponse<UserResponse> createUser(UserRequest request) {
        if(userExists(request.getUsername())){
            throw new BookStoreException("User already exist");
        }
        User user = mapUserRequestToModel(request);
        User savedUser = userRepository.save(user);
        return HttpResponse.<UserResponse>builder()
                .data(mapUserModelToResponse(savedUser))
                .statusCode(201)
                .status(HttpStatus.CREATED)
                .message("User successfully created")
                .build();
    }

    @PostConstruct
        public void preloadUsers(){
        if(userRepository.count() < 1) {
            User user1 = new User();
            user1.setUsername("username1");
            User user2 = new User();
            user2.setUsername("username2");
            List<User> userList = List.of(user1, user2);

            userRepository.saveAll(userList);
        }
    }
}
