package com.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping("/hello")
    public String hello(Model m) {
        m.addAttribute("name", "thymeleaf");
        return "hello";
    }

    @RequestMapping("/register")
    public String register(Model m) {
        return "register";
    }

    @RequestMapping("/login")
    public String login(Model m) {
        return "login";
    }

    @RequestMapping("/index")
    public String index(Model m) {
        return "index";
    }

    @RequestMapping("/detail")
    public String detail(Model m) {
        return "detail";
    }

    @RequestMapping("/cart")
    public String cart(Model m) {
        return "cart";
    }

    @RequestMapping("/order")
    public String order(Model m) {
        return "order";
    }

    @RequestMapping("/myinfo")
    public String myinfo(Model m) {
        return "myinfo";
    }

    @RequestMapping("/account")
    public String account(Model m) {
        return "account";
    }

    @RequestMapping("/favorite")
    public String favorite(Model m) {
        return "favorite";
    }

    @RequestMapping("/myaddress")
    public String myaddress(Model m) {
        return "myaddress";
    }

    @RequestMapping("/notice")
    public String notice(Model m) {
        return "notice";
    }

}
