package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MainController {

    @Autowired
    UserService userService;

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<User> main(@PathVariable Long id){
        User user = userService.getUser(id);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<User> join(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "email") String email){

        userService.join(name, email);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
