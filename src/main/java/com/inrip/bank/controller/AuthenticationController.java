package com.inrip.bank.controller;

import com.inrip.bank.controller.handlers.SimpleBankHTTPResponseHandler;
import com.inrip.bank.model.User;
import com.inrip.bank.security.JwtTokenUtil;
import com.inrip.bank.service.user.UserService;
import com.inrip.bank.dto.AuthToken;
import com.inrip.bank.dto.LoginUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController  extends SimpleBankHTTPResponseHandler {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody LoginUser loginUser) throws AuthenticationException {
        final String token;
        final User user;
    
        final Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    loginUser.getUsername(),
                    loginUser.getPassword()
            )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        user = userService.findOne(loginUser.getUsername());
        token = jwtTokenUtil.generateToken(user);            
           
        return ResponseEntity.ok(new AuthToken(token));    
    }

}
