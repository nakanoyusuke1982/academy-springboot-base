package com.spring.springbootapplication.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private String profile;
    private String imageUrl;
}