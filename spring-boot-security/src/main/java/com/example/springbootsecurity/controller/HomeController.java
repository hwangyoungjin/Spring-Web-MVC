package com.example.springbootsecurity.controller;

import com.example.springbootsecurity.account.Account;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/hello")
    public String  hello(){
        return "hello";
    }

    @GetMapping("/my")
    public String my(Model model, Authentication authentication){
        model.addAttribute("name",authentication.getName());

        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("name1",a.getName());
        return "my";
    }
}
