package com.example.java_task.controllers;


import com.example.java_task.dto.RegistrationDto;
import com.example.java_task.dto.UserDto;
import com.example.java_task.entities.User;
import com.example.java_task.repositories.UserRepository;
import com.example.java_task.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final UserRepository userRepository;


    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterForm(Model model) {
        RegistrationDto user = new RegistrationDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String register(
            @ModelAttribute("user") UserDto user,
            BindingResult result, Model model
    ) {
        Optional<User> byUsername = userRepository.findByUsername(user.getUsername());
        if (byUsername.isEmpty()) {
            userService.saveUser(user);
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "redirect/:register?fail";
    }
}
