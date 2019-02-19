package com.example.demo.domain.repository;

import com.example.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllBy();
}
