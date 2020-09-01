package com.rakib.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/")
public class HomeController {
    @GetMapping
    public String index(Model model){
        return "index";
    }
    @GetMapping("blog")
    public String blog(Model model){
        return "blog";
    }
    @GetMapping("login")
    public String login(Model model){
        return "login";
    }
}
