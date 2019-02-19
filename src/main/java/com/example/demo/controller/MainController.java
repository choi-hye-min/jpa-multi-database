package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @Autowired
    UserService userService;

    @GetMapping(value = "/{id}")
    public User main(@PathVariable Long id){
        return userService.getUser(id);
    }
}
