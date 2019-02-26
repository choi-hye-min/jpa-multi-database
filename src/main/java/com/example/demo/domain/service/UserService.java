package com.example.demo.domain.service;

import com.example.demo.domain.User;
import com.example.demo.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getUser(Long id){
        Optional<User> user = userRepository.findById(id);
        return user.isPresent() ? user.get() : null;
    }

    @Transactional
    public void join(String name, String email) {
        User user = User.builder()
                .createAt(LocalDateTime.now())
                .email(email)
                .name(name)
                .build();

        userRepository.save(user);
    }
}
