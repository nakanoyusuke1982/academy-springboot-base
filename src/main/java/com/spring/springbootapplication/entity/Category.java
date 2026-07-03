package com.spring.springbootapplication.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Category {

    private Integer id;
    private String name;
    private LocalDateTime createdAt;
}
