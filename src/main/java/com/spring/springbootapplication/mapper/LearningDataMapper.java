package com.spring.springbootapplication.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.spring.springbootapplication.entity.LearningData;

@Mapper
public interface LearningDataMapper {

    List<LearningData> findByUserIdAndStudyDateBetween(
        @Param("userId") Integer userId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}