package com.c_code.bate_ponto.service;

import com.c_code.bate_ponto.model.*;
import com.c_code.bate_ponto.repository.*;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String name, String email, UserType type) {

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setType(type);


        return userRepository.save(user);
    }
}
