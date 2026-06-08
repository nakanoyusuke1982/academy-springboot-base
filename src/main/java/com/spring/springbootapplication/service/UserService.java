package com.spring.springbootapplication.service;

import org.springframework.stereotype.Service;

import com.spring.springbootapplication.form.UserForm;
import com.spring.springbootapplication.mapper.UserMapper;
import com.spring.springbootapplication.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public void createUser(UserForm form) {

         User user = new User();

          user.setName(form.getName());
        user.setEmail(form.getEmail());
        
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(form.getPassword());
        user.setPassword(hashedPassword);

        userMapper.insert(user);
}

}