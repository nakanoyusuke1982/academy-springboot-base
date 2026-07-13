package com.spring.springbootapplication.entity;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillChartData {

    private LocalDate studyDate;

    private String categoryName;

    private Integer totalStudyTime;
    
}