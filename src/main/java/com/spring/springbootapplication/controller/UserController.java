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
import com.spring.springbootapplication.service.UserService;
import jakarta.servlet.http.HttpSession;

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
public String createUser(@ModelAttribute UserForm userForm) {
    userService.createUser(userForm);
    return "redirect:/signup";
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
public String top(HttpSession session) {
    if (session.getAttribute("loginUser") == null) {
        return "redirect:/login";
    }

    return "top";
}

@PostMapping("/logout")
public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/login";
}

}