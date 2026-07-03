package com.spring.springbootapplication.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.spring.springbootapplication.entity.LearningData;
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
}