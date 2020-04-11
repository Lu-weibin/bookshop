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

    @RequestMapping("/register")
    public String register() {
        return "register";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/detail/{bookid}")
    public String detail(@PathVariable Integer bookid, Model m) {
        m.addAttribute("bookid", bookid);
        return "detail";
    }

    @RequestMapping("/cart")
    public String cart() {
        return "cart";
    }

    @RequestMapping("/order")
    public String order() {
        return "order";
    }

    @RequestMapping("/myinfo")
    public String myinfo() {
        return "myinfo";
    }

    @RequestMapping("/account")
    public String account() {
        return "account";
    }

    @RequestMapping("/favorite")
    public String favorite() {
        return "favorite";
    }

    @RequestMapping("/myaddress")
    public String myaddress() {
        return "myaddress";
    }

    @RequestMapping("/notice")
    public String notice() {
        return "notice";
    }

    @RequestMapping("logout")
    public String logout() {
        request.getSession().setAttribute("username", null);
        request.getSession().setAttribute("userid", null);
        return "redirect:index";
    }
}
