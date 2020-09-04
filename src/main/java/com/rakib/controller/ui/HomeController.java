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
    @GetMapping("login/user")
    public String user(Model model){
        return "user";
    }
    @GetMapping("login/user/blog")
    public String userBlog(Model model){
        return "userblog";
    }
    @GetMapping("login/user/all")
    public String userAllBlog(Model model){
        return "userblogall";
    }
    @GetMapping("login/admin")
    public String admin(Model model){
        return "admin";
    }

    @GetMapping("login/admin/allblogger")
    public String adminAllBlogger(Model model){
        return "adminbloggers";
    }
    @GetMapping("login/admin/pendingblogger")
    public String adminPendingBlogger(Model model){
        return "adminpendingbloggers";
    }
    @GetMapping("login/admin/pendingblog")
    public String adminPendingBlog(Model model){
        return "adminpendingblog";
    }
    @GetMapping("/login/admin/add")
    public String adminCreate(Model model){
        return "admincreate";
    }
}
