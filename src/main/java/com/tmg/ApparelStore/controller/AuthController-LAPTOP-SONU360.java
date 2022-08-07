package com.tmg.ApparelStore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.tmg.ApparelStore.dao.service.UserService;
import com.tmg.ApparelStore.dto.UserDto;

@Controller
public class AuthController {
	@Autowired
    private UserService userService;


    @ModelAttribute("user")
    UserDto userDto()
    {
        return new UserDto();
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("user") UserDto dto) {
        if(userService.isRegistered(dto))
            return "redirect:/signup?error";
        userService.save(dto);
        return "redirect:/login";
//        return "redirect:/signup?success";
    }
}
