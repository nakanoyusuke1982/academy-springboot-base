package com.spring.springbootapplication.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.springbootapplication.entity.LearningData;
import com.spring.springbootapplication.form.LearningDataForm;
import com.spring.springbootapplication.service.LearningDataService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
            learningDataService.findMonthlyLearningData(
                    userId,
                    targetDate
            );

    List<LearningData> backendList = new ArrayList<>();
    List<LearningData> frontendList = new ArrayList<>();
    List<LearningData> infraList = new ArrayList<>();

    for (LearningData data : learningDataList) {

        if ("バックエンド".equals(data.getCategoryName())) {
            backendList.add(data);

        } else if ("フロントエンド".equals(data.getCategoryName())) {
            frontendList.add(data);

        } else if ("インフラ".equals(data.getCategoryName())) {
            infraList.add(data);
        }
    }

    List<YearMonth> monthList = new ArrayList<>();
    YearMonth currentMonth = YearMonth.now();

    for (int i = 0; i < 3; i++) {
        monthList.add(currentMonth.minusMonths(i));
    }

    Integer infraCategoryId =
        learningDataService.findCategoryIdByName("インフラ");

    Integer backendCategoryId =
        learningDataService.findCategoryIdByName("バックエンド");

    Integer frontendCategoryId =
        learningDataService.findCategoryIdByName("フロントエンド");

    model.addAttribute("learningDataList", learningDataList);
    model.addAttribute("backendList", backendList);
    model.addAttribute("frontendList", frontendList);
    model.addAttribute("infraList", infraList);
    model.addAttribute("monthList", monthList);
    model.addAttribute("selectedMonth", targetMonth);
    model.addAttribute("infraCategoryId", infraCategoryId);
    model.addAttribute("backendCategoryId", backendCategoryId);
    model.addAttribute("frontendCategoryId", frontendCategoryId);

    return "learning-data/list";
}

@GetMapping("/learning-data/new")
public String newItem(
        @RequestParam(name = "month") String month,
        @RequestParam(name = "categoryId") Integer categoryId,
        Model model,
        HttpSession session) {

    if (session.getAttribute("loginUser") == null) {
        return "redirect:/login";
    }

    YearMonth selectedMonth =
            YearMonth.parse(month);

    LearningDataForm form =
            new LearningDataForm();

    form.setMonth(month);
    form.setCategoryId(categoryId);

    Integer infraCategoryId =
            learningDataService.findCategoryIdByName("インフラ");

    Integer backendCategoryId =
            learningDataService.findCategoryIdByName("バックエンド");

    Integer frontendCategoryId =
            learningDataService.findCategoryIdByName("フロントエンド");

    String categoryName = "";

    if (categoryId.equals(infraCategoryId)) {
        categoryName = "インフラ";

    } else if (categoryId.equals(backendCategoryId)) {
        categoryName = "バックエンド";

    } else if (categoryId.equals(frontendCategoryId)) {
        categoryName = "フロントエンド";
    }

    model.addAttribute(
            "selectedMonth",
            selectedMonth
    );

    model.addAttribute(
            "learningDataForm",
            form
    );

    model.addAttribute(
            "categoryName",
            categoryName
    );

    return "learning-data/new";
}
@PostMapping("/learning-data")
public String createLearningData(
        @Valid @ModelAttribute("learningDataForm") LearningDataForm form,
        BindingResult bindingResult,
        HttpSession session,
        Model model) {

    if (session.getAttribute("loginUser") == null) {
        return "redirect:/login";
    }

    Integer userId = 1;

    /*
     * Bean Validationでエラーがある場合
     * 空欄、50文字超過、0未満など
     */
    if (bindingResult.hasErrors()) {
        setNewPageModel(model, form);

        return "learning-data/new";
    }

    /*
     * 同じ月に同じ項目名が登録されていないか確認
     */
    String errorMessage =
            learningDataService.validate(
                    form,
                    userId
            );

    if (errorMessage != null) {
        bindingResult.rejectValue(
                "itemName",
                "duplicate",
                errorMessage
        );

        setNewPageModel(model, form);

        return "learning-data/new";
    }

    learningDataService.createLearningData(
            form,
            userId
    );

    model.addAttribute(
            "selectedMonth",
            YearMonth.parse(form.getMonth())
    );

    setNewPageModel(model, form);

    String successMessage =
            form.getItemName()
            + " を"
            + form.getStudyTime()
            + "分で追加しました！";

    model.addAttribute(
            "successMessage",
            successMessage
    );

    LearningDataForm newForm =
            new LearningDataForm();

    newForm.setMonth(form.getMonth());
    newForm.setCategoryId(
            form.getCategoryId()
    );

    model.addAttribute(
            "learningDataForm",
            newForm
    );

    return "learning-data/new";
}
private void setNewPageModel(
        Model model,
        LearningDataForm form) {

    Integer infraCategoryId =
            learningDataService.findCategoryIdByName("インフラ");

    Integer backendCategoryId =
            learningDataService.findCategoryIdByName("バックエンド");

    Integer frontendCategoryId =
            learningDataService.findCategoryIdByName("フロントエンド");

    String categoryName = "";

    if (form.getCategoryId().equals(infraCategoryId)) {
        categoryName = "インフラ";

    } else if (form.getCategoryId().equals(backendCategoryId)) {
        categoryName = "バックエンド";

    } else if (form.getCategoryId().equals(frontendCategoryId)) {
        categoryName = "フロントエンド";
    }

    model.addAttribute(
            "selectedMonth",
            YearMonth.parse(form.getMonth())
    );

    model.addAttribute(
            "categoryName",
            categoryName
    );
}
}