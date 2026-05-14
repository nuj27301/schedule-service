package com.schedule.controller;

import com.schedule.dto.MemberDto;
import com.schedule.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute MemberDto dto, Model model) {
    try {
        memberService.register(dto);
        return "redirect:/login";
    } catch (RuntimeException e) {
        model.addAttribute("error", e.getMessage());
        return "register";
    }
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }
}