package com.web.controller;

import com.web.pojo.Notice;
import com.web.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PageController {

    private final HttpServletRequest request;
    private final NoticeService noticeService;

    public PageController(HttpServletRequest request, NoticeService noticeService) {
        this.request = request;
        this.noticeService = noticeService;
    }

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

    @RequestMapping("/detail/{bookId}")
    public String detail(@PathVariable Integer bookId, Model m) {
        m.addAttribute("bookId", bookId);
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

    @RequestMapping("/mybook")
    public String mybook() {
        return "mybook";
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
        request.getSession().setAttribute("userId", null);
        return "redirect:index";
    }


    @RequestMapping("admin/welcome")
    public String welcome() {
        return "admin/welcome";
    }

    @RequestMapping("admin/category")
    public String category(Model model) {
        model.addAttribute("categoryActive", "list-group-item active");
        return "admin/category";
    }

    @RequestMapping("admin/book")
    public String book(Model model) {
        model.addAttribute("bookActive", "list-group-item active");
        return "admin/book";
    }

    @RequestMapping("admin/user")
    public String user(Model model) {
        model.addAttribute("userActive", "list-group-item active");
        return "admin/user";
    }

    @RequestMapping("admin/notice")
    public String adminNotice(Model model) {
        model.addAttribute("noticeActive", "list-group-item active");
        return "admin/notice";
    }

    @RequestMapping({"admin/editNotice", "admin/editNotice/{noticeId}"})
    public String editNotice(@PathVariable(required = false) Integer noticeId, Model model) {
        Notice notice = new Notice();
        if (noticeId != null) {
            notice = noticeService.findById(noticeId).orElse(new Notice());
        }
        model.addAttribute("notice", notice);
        model.addAttribute("editNoticeActive", "list-group-item active");
        return "admin/editNotice";
    }

}
