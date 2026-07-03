package com.spring.springbootapplication.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.springbootapplication.entity.LearningData;
import com.spring.springbootapplication.service.LearningDataService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LearningDataController {

    private final LearningDataService learningDataService;

    @GetMapping("/learning-data")
    public String list(
            @RequestParam(name = "month", required = false) String month,
            Model model) {

        Integer userId = 1;

        YearMonth targetMonth = month == null
                ? YearMonth.now()
                : YearMonth.parse(month);

        LocalDate targetDate = targetMonth.atDay(1);

        List<LearningData> learningDataList =
                learningDataService.findMonthlyLearningData(userId, targetDate);

        List<YearMonth> monthList = new ArrayList<>();
        YearMonth currentMonth = YearMonth.now();

        for (int i = 0; i < 4; i++) {
            monthList.add(currentMonth.minusMonths(i));
        }

        model.addAttribute("learningDataList", learningDataList);
        model.addAttribute("monthList", monthList);
        model.addAttribute("selectedMonth", targetMonth);

        return "learning-data/list";
    }
}