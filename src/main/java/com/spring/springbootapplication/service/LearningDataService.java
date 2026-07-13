package com.spring.springbootapplication.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
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

    public Integer findCategoryIdByName(String categoryName) {
      return learningDataMapper.findCategoryIdByName(categoryName);
    }

    /**
     * 指定した月の学習データを取得する
     */
    public List<LearningData> findMonthlyLearningData(
            Integer userId,
            LocalDate targetMonth) {

        LocalDate startDate = targetMonth.withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(1);

        return learningDataMapper.findByUserIdAndStudyDateBetween(
                userId,
                startDate,
                endDate
        );
    }

    /**
     * 学習項目登録時の入力チェック
     */
    public String validate(LearningDataForm form, Integer userId) {

        // カテゴリの選択確認
        if (form.getCategoryId() == null) {
            return "カテゴリを選択してください";
        }

        // 項目名の入力確認
        if (form.getItemName() == null
                || form.getItemName().isBlank()) {
            return "項目名は必ず入力してください";
        }

        // 項目名の文字数確認
        if (form.getItemName().trim().length() > 50) {
            return "項目名は50文字以内で入力してください";
        }

        // 学習時間の確認
        if (form.getStudyTime() == null
                || form.getStudyTime() < 0) {
            return "学習時間は0以上の数字で入力してください";
        }

        // 対象月の確認
        if (form.getMonth() == null
                || form.getMonth().isBlank()) {
            return "対象月が選択されていません";
        }

        YearMonth targetMonth;

        try {
            targetMonth = YearMonth.parse(form.getMonth());
        } catch (DateTimeParseException e) {
            return "対象月の形式が正しくありません";
        }

        LocalDate startDate = targetMonth.atDay(1);
        LocalDate endDate = startDate.plusMonths(1);

        // 同じ月・同じカテゴリ・同じ項目名の重複確認
        int count =
                learningDataMapper
                        .countByUserIdAndCategoryIdAndItemNameAndMonth(
                                userId,
                                form.getCategoryId(),
                                form.getItemName().trim(),
                                startDate,
                                endDate
                        );

        if (count > 0) {
            return form.getItemName().trim()
                    + "は既に登録されています";
        }

        return null;
    }

    /**
     * 学習項目を登録する
     */
        public void createLearningData(
            LearningDataForm form,
            Integer userId) {

        YearMonth targetMonth =
                YearMonth.parse(form.getMonth());

        LocalDate studyDate =
                targetMonth.atDay(1);

        learningDataMapper.insertLearningData(
                userId,
                form.getCategoryId(),
                form.getItemName().trim(),
                form.getStudyTime(),
                studyDate
        );
    }

    /**
     * 学習時間を更新する
     */
    public void updateStudyTime(
            Integer id,
            Integer userId,
            Integer studyTime) {

        learningDataMapper.updateStudyTime(
                id,
                userId,
                studyTime
        );
    }
    /**
 * IDとユーザーIDから学習データを1件取得する
 */
public LearningData findByIdAndUserId(
        Integer id,
        Integer userId) {

    return learningDataMapper.findByIdAndUserId(
            id,
            userId
    );
}

/**
 * IDとユーザーIDを指定して学習データを削除する
 */
public void deleteLearningData(
        Integer id,
        Integer userId) {

    learningDataMapper.deleteByIdAndUserId(
            id,
            userId
    );
}

}
