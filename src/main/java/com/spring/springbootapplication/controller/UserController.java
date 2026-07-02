package com.spring.springbootapplication.controller;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.springbootapplication.entity.User;
import com.spring.springbootapplication.form.UserForm;
import com.spring.springbootapplication.form.ProfileForm;
import com.spring.springbootapplication.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UserController {

private final UserService userService;

public UserController(UserService userService) {
    this.userService = userService;
}

@GetMapping("/signup")
public String signup(Model model) {
    model.addAttribute("userForm", new UserForm());
    return "signup";
}

@PostMapping("/signup")
public String createUser(@ModelAttribute UserForm userForm, HttpSession session) {
    userService.createUser(userForm);

    User user = userService.findByEmail(userForm.getEmail());
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
public String top(HttpSession session, Model model) {
    User loginUser = (User) session.getAttribute("loginUser");

    if (loginUser == null) {
        return "redirect:/login";
    }

    model.addAttribute("loginUser", loginUser);
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
        @Valid @ModelAttribute ProfileForm profileForm,
        BindingResult bindingResult,
        HttpSession session,
        Model model) {

    User loginUser = (User) session.getAttribute("loginUser");

    if (loginUser == null) {
        return "redirect:/login";
    }

    if (bindingResult.hasErrors()) {
    System.out.println(bindingResult.getAllErrors());

    model.addAttribute("profileForm", profileForm);
    model.addAttribute("loginUser", loginUser);

    return "profile-edit";
}

    loginUser.setProfile(profileForm.getProfile());

MultipartFile imageFile = profileForm.getImageFile();

if (imageFile != null && !imageFile.isEmpty()) {
    loginUser.setImageUrl(imageFile.getOriginalFilename());
}

userService.updateProfile(loginUser);

    session.setAttribute("loginUser", loginUser);

    return "redirect:/top";
}

@PostMapping("/logout")
public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/login";
}

}