package com.spring.springbootapplication.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.spring.springbootapplication.entity.User;

@Mapper
public interface UserMapper {
    void insert(User user);
    User findByEmail(String email);
}