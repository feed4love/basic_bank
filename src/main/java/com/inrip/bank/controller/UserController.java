package com.inrip.bank.controller;

import com.inrip.bank.controller.handlers.SimpleBankHTTPResponseHandler;
import com.inrip.bank.model.User;
import com.inrip.bank.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController  extends SimpleBankHTTPResponseHandler {
    @Autowired
    UserService userService;

    @PostMapping("/register")
    ResponseEntity<?> registerUser(@RequestBody User request) {
        User response = userService.createUser(request);
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }
    
}
