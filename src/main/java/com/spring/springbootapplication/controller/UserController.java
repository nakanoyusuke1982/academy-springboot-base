package com.spring.springbootapplication.controller;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.springbootapplication.entity.SkillChartData;
import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.form.ProfileForm;
import com.spring.springbootapplication.form.UserForm;
import com.spring.springbootapplication.service.LearningDataService;
import com.spring.springbootapplication.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.time.YearMonth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
public class UserController {

    private final UserService userService;
    private final LearningDataService learningDataService;

    public UserController(
            UserService userService,
            LearningDataService learningDataService) {

        this.userService = userService;
        this.learningDataService = learningDataService;
    }

@GetMapping("/signup")
public String signup(Model model) {
    model.addAttribute("userForm", new UserForm());
    return "signup";
}

@PostMapping("/signup")
public String createUser(
        @Valid @ModelAttribute("userForm") UserForm userForm,
        BindingResult bindingResult,
        HttpSession session) {

    if (!bindingResult.hasFieldErrors("email")) {

        User existingUser =
                userService.findByEmail(userForm.getEmail());

        if (existingUser != null) {
            bindingResult.rejectValue(
                    "email",
                    "duplicate",
                    "既に登録されているメールアドレスです"
            );
        }
    }

    if (bindingResult.hasErrors()) {
        return "signup";
    }

    userService.createUser(userForm);

    User user =
            userService.findByEmail(userForm.getEmail());

    session.setAttribute("loginUser", user);

    return "redirect:/top";
}

@GetMapping("/login")
public String login() {
    return "login";
}

@PostMapping("/login")
public String login(
        @RequestParam String email,
        @RequestParam String password,
        RedirectAttributes redirectAttributes,
        HttpSession session) {

    User user = userService.findByEmail(email);

    if (user == null) {
        redirectAttributes.addFlashAttribute("errorMessage", "メールアドレス、もしくはパスワードが間違っています");
        return "redirect:/login";
    }

    if (!BCrypt.checkpw(password, user.getPassword())) {
        redirectAttributes.addFlashAttribute("errorMessage", "メールアドレス、もしくはパスワードが間違っています");
        return "redirect:/login";
    }
    session.setAttribute("loginUser", user);
    return "redirect:/top";
}

@GetMapping("/top")
public String top(
        HttpSession session,
        Model model) {

    User loginUser =
            (User) session.getAttribute("loginUser");

    if (loginUser == null) {
        return "redirect:/login";
    }

    List<SkillChartData> skillChartDataList =
            learningDataService.findCategoryStudyTimeTotals(
                    loginUser.getId()
            );

    Integer[] backendData = {0, 0, 0};
    Integer[] frontendData = {0, 0, 0};
    Integer[] infraData = {0, 0, 0};

    YearMonth currentMonth =
            YearMonth.now();

    for (SkillChartData data : skillChartDataList) {

    if (data.getStudyDate() == null) {
        continue;
    }

    YearMonth studyMonth =
            YearMonth.from(data.getStudyDate());

    int monthIndex;

    if (studyMonth.equals(
            currentMonth.minusMonths(2))) {

        monthIndex = 0;

    } else if (studyMonth.equals(
            currentMonth.minusMonths(1))) {

        monthIndex = 1;

    } else if (studyMonth.equals(
            currentMonth)) {

        monthIndex = 2;

    } else {
        continue;
    }

    if ("バックエンド".equals(data.getCategoryName())) {

        backendData[monthIndex] =
                data.getTotalStudyTime();

    } else if ("フロントエンド".equals(data.getCategoryName())) {

        frontendData[monthIndex] =
                data.getTotalStudyTime();

    } else if ("インフラ".equals(data.getCategoryName())) {

        infraData[monthIndex] =
                data.getTotalStudyTime();
    }
}

    model.addAttribute(
            "loginUser",
            loginUser
    );

    model.addAttribute(
            "backendData",
            backendData
    );

    model.addAttribute(
            "frontendData",
            frontendData
    );

    model.addAttribute(
            "infraData",
            infraData
    );

    return "top";
}

@GetMapping("/profile/edit")
public String editProfile(HttpSession session, Model model) {
    User loginUser = (User) session.getAttribute("loginUser");

    if (loginUser == null) {
        return "redirect:/login";
    }

   ProfileForm profileForm = new ProfileForm();
profileForm.setProfile(loginUser.getProfile());

model.addAttribute("profileForm", profileForm);
    model.addAttribute("loginUser", loginUser);

    return "profile-edit";
}

@PostMapping("/profile/edit")
public String updateProfile(
        @Valid @ModelAttribute("profileForm") ProfileForm profileForm,
        BindingResult bindingResult,
        HttpSession session,
        Model model) {

    User loginUser =
            (User) session.getAttribute("loginUser");

    if (loginUser == null) {
        return "redirect:/login";
    }

    if (bindingResult.hasErrors()) {
        model.addAttribute("loginUser", loginUser);
        return "profile-edit";
    }

    MultipartFile imageFile =
            profileForm.getImageFile();

    if (imageFile != null && !imageFile.isEmpty()) {

        long maxFileSize =
                5L * 1024 * 1024;

        if (imageFile.getSize() > maxFileSize) {

            bindingResult.rejectValue(
                    "imageFile",
                    "fileSize",
                    "プロフィール画像は5MB以下のファイルを選択してください"
            );

            model.addAttribute(
                    "loginUser",
                    loginUser
            );

            return "profile-edit";
        }

        try {
            Path uploadDirectory =
                    Paths.get(
                            System.getProperty("user.dir"),
                            "uploads"
                    );

            Files.createDirectories(
                    uploadDirectory
            );

            String originalFilename =
                    imageFile.getOriginalFilename();

            String extension = "";

            if (originalFilename != null
                    && originalFilename.contains(".")) {

                extension =
                        originalFilename.substring(
                                originalFilename.lastIndexOf(".")
                        );
            }

            String savedFilename =
                    UUID.randomUUID() + extension;

            Path savePath =
                    uploadDirectory.resolve(
                            savedFilename
                    );

            imageFile.transferTo(
                    savePath.toFile()
            );

            loginUser.setImageUrl(
                    savedFilename
            );

        } catch (IOException e) {

            bindingResult.rejectValue(
                    "imageFile",
                    "uploadError",
                    "プロフィール画像の保存に失敗しました"
            );

            model.addAttribute(
                    "loginUser",
                    loginUser
            );

            return "profile-edit";
        }
    }

    // 画像チェックと保存が成功した後に更新
    loginUser.setProfile(
            profileForm.getProfile()
    );

    userService.updateProfile(
            loginUser
    );

    session.setAttribute(
            "loginUser",
            loginUser
    );

    return "redirect:/top";
}

    
@PostMapping("/logout")
public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/login";
}

@GetMapping("/logout")
public String logoutGet(HttpSession session) {
    session.invalidate();
    return "redirect:/login";
}

}