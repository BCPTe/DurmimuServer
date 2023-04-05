package com.durmimumacares.website.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public Optional<List<User>> userByName(String name) {
        return userRepository.findUserByName(name);
    }

    public Optional<List<User>> userBySurname(String surname) { return userRepository.findUserBySurname(surname); }

    public Optional<List<User>> userByEmail(String email) { return userRepository.findUserByEmail(email); }

    public User newUser(User user) { return userRepository.insert(user); }
}
