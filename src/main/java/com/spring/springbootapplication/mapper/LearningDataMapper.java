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

    Integer findCategoryIdByName(@Param("categoryName") String categoryName);

void insertCategory(@Param("categoryName") String categoryName);

void insertLearningData(
    @Param("userId") Integer userId,
    @Param("categoryId") Integer categoryId,
    @Param("studyTime") Integer studyTime,
    @Param("studyDate") LocalDate studyDate
);

int countByUserIdAndCategoryNameAndMonth(
    @Param("userId") Integer userId,
    @Param("categoryName") String categoryName,
    @Param("startDate") LocalDate startDate,
    @Param("endDate") LocalDate endDate
);
}