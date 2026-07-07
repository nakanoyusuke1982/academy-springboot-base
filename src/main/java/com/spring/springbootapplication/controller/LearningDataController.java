package com.spring.springbootapplication.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.springbootapplication.entity.LearningData;
import com.spring.springbootapplication.form.LearningDataForm;
import com.spring.springbootapplication.service.LearningDataService;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class LearningDataController {

    private final LearningDataService learningDataService;

   @GetMapping("/learning-data")
public String list(
        @RequestParam(name = "month", required = false) String month,
        Model model,
        HttpSession session) {

    
    if (session.getAttribute("loginUser") == null) {
        return "redirect:/login";
    }

    Integer userId = 1;

    YearMonth targetMonth = month == null
            ? YearMonth.now()
            : YearMonth.parse(month);

    LocalDate targetDate = targetMonth.atDay(1);

    List<LearningData> learningDataList =
            learningDataService.findMonthlyLearningData(userId, targetDate);

    List<YearMonth> monthList = new ArrayList<>();
    YearMonth currentMonth = YearMonth.now();

    for (int i = 0; i < 3; i++) {
        monthList.add(currentMonth.minusMonths(i));
    }

    model.addAttribute("learningDataList", learningDataList);
    model.addAttribute("monthList", monthList);
    model.addAttribute("selectedMonth", targetMonth);

    return "learning-data/list";
}
  @GetMapping("/learning-data/new")
public String newItem(
        @RequestParam(name = "month") String month,
        Model model,
        HttpSession session) {

    if (session.getAttribute("loginUser") == null) {
        return "redirect:/login";
    }

    YearMonth selectedMonth = YearMonth.parse(month);

    model.addAttribute("selectedMonth", selectedMonth);

    return "learning-data/new";
}

 @PostMapping("/learning-data")
public String create(
        @ModelAttribute LearningDataForm form,
        Model model,
        HttpSession session) {

    if (session.getAttribute("loginUser") == null) {
        return "redirect:/login";
    }

    Integer userId = 1;

    // 以下は今の処理のままでOK


    String errorMessage = learningDataService.validate(form, userId);

    if (errorMessage != null) {
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("selectedMonth", YearMonth.parse(form.getMonth()));
        model.addAttribute("learningDataForm", form);

        return "learning-data/new";
    }

    learningDataService.createLearningData(form, userId);

    model.addAttribute("selectedMonth", YearMonth.parse(form.getMonth()));
    String successMessage =
        form.getCategoryName()
        + " に\n"
        + form.getStudyTime()
        + "分で追加しました！";

model.addAttribute("successMessage", successMessage);

    return "learning-data/new";
}
}
