package com.spring.springbootapplication.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import com.spring.springbootapplication.entity.LearningData;
import com.spring.springbootapplication.form.LearningDataForm;
import com.spring.springbootapplication.mapper.LearningDataMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LearningDataService {

    private final LearningDataMapper learningDataMapper;

    public List<LearningData> findMonthlyLearningData(Integer userId, LocalDate targetMonth) {
        LocalDate startDate = targetMonth.withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(1);

        return learningDataMapper.findByUserIdAndStudyDateBetween(userId, startDate, endDate);
    }

    public String validate(LearningDataForm form, Integer userId) {
        if (form.getCategoryName() == null || form.getCategoryName().isBlank()) {
            return "項目名は必ず入力してください";
        }

        if (form.getCategoryName().length() > 50) {
            return "項目名は50文字以内で入力してください";
        }

        if (form.getStudyTime() == null || form.getStudyTime() < 0) {
            return "学習時間は0以上の数字で入力してください";
        }

        YearMonth targetMonth = YearMonth.parse(form.getMonth());
        LocalDate startDate = targetMonth.atDay(1);
        LocalDate endDate = startDate.plusMonths(1);

        int count = learningDataMapper.countByUserIdAndCategoryNameAndMonth(
                userId,
                form.getCategoryName(),
                startDate,
                endDate
        );

        if (count > 0) {
            return form.getCategoryName() + "は既に登録されています";
        }

        return null;
    }

    public void createLearningData(LearningDataForm form, Integer userId) {
        YearMonth targetMonth = YearMonth.parse(form.getMonth());
        LocalDate studyDate = targetMonth.atDay(1);

        Integer categoryId = learningDataMapper.findCategoryIdByName(form.getCategoryName());

        if (categoryId == null) {
            learningDataMapper.insertCategory(form.getCategoryName());
            categoryId = learningDataMapper.findCategoryIdByName(form.getCategoryName());
        }

        learningDataMapper.insertLearningData(
                userId,
                categoryId,
                form.getStudyTime(),
                studyDate
        );
    }
}