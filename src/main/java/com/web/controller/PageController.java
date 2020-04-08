package com.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PageController {

    @Autowired
    private HttpServletRequest request;

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

    @RequestMapping("/detail/{bookid}")
    public String detail(@PathVariable Integer bookid, Model m) {
        m.addAttribute("bookid", bookid);
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

    @RequestMapping("logout")
    public String logout() {
        request.getSession().setAttribute("username", null);
        return "redirect:index";
    }
}
