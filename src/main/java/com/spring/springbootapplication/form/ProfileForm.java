package com.spring.springbootapplication.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProfileForm {

    @NotBlank(message = "自己紹介は50文字以上200文字以下で入力してください")
    @Size(min = 50, max = 200, message = "自己紹介は50文字以上200文字以下で入力してください")
    private String profile;

    private MultipartFile imageFile;
}