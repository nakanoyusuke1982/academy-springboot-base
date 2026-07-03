package com.spring.springbootapplication.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class LearningData {

    private Integer id;
    private Integer userId;
    private Integer categoryId;
    private Integer studyTime;
    private LocalDate studyDate;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

    private String categoryName;
}