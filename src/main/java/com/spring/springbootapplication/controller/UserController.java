package com.spring.springbootapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import com.spring.springbootapplication.form.UserForm;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.spring.springbootapplication.service.UserService;

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

}