package com.example.sprinkler.apiserver.services;

import com.example.sprinkler.apiserver.entities.User;
import com.example.sprinkler.apiserver.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UserRepository userRepository;
    public User getUserByUsername(String username){
        return userRepository.findByEmail(username).orElseThrow();
    }

}
