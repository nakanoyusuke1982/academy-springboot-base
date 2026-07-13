package com.spring.springbootapplication.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.spring.springbootapplication.entity.LearningData;

@Mapper
public interface LearningDataMapper {

    // 指定したユーザー・対象月の学習項目を取得
    List<LearningData> findByUserIdAndStudyDateBetween(
        @Param("userId") Integer userId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    // 学習項目を登録
    void insertLearningData(
        @Param("userId") Integer userId,
        @Param("categoryId") Integer categoryId,
        @Param("itemName") String itemName,
        @Param("studyTime") Integer studyTime,
        @Param("studyDate") LocalDate studyDate
    );

    // 同じ月・同じカテゴリに同じ項目名が存在するか確認
    int countByUserIdAndCategoryIdAndItemNameAndMonth(
        @Param("userId") Integer userId,
        @Param("categoryId") Integer categoryId,
        @Param("itemName") String itemName,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    Integer findCategoryIdByName(
    @Param("categoryName") String categoryName
);

// 学習時間を更新
void updateStudyTime(
    @Param("id") Integer id,
    @Param("userId") Integer userId,
    @Param("studyTime") Integer studyTime
);

// IDとユーザーIDから学習データを1件取得
LearningData findByIdAndUserId(
    @Param("id") Integer id,
    @Param("userId") Integer userId
);

// IDとユーザーIDを指定して学習データを削除
void deleteByIdAndUserId(
    @Param("id") Integer id,
    @Param("userId") Integer userId
);
}