package com.inrip.bank.service.user;

import com.inrip.bank.model.User;

public interface UserService {
    User findOne(String username);
    User createUser(User user);
}
