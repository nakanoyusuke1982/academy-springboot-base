package com.spring.springbootapplication.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class LearningData {

    private Integer id;
    private Integer userId;
    private Integer categoryId;

    // Java、Ruby、AWSなどの項目名
    private String itemName;

    private Integer studyTime;
    private LocalDate studyDate;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

    // categoriesテーブルから取得するカテゴリ名
    private String categoryName;
}