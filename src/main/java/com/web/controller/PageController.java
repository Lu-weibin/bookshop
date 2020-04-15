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

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private NoticeService noticeService;

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
        request.getSession().setAttribute("userid", null);
        return "redirect:index";
    }

    @RequestMapping("admin/category")
    public String category() {
        return "admin/category";
    }

    @RequestMapping("admin/book")
    public String book() {
        return "admin/book";
    }

    @RequestMapping("admin/user")
    public String user() {
        return "admin/user";
    }

    @RequestMapping("admin/notice")
    public String adminNotice() {
        return "admin/notice";
    }

//    @RequestMapping("admin/editNotice")
//    public String editNotice(Model model) {
//        model.addAttribute("notice", new Notice());
//        return "admin/editNotice";
//    }

    @RequestMapping({"admin/editNotice", "admin/editNotice/{noticeId}"})
    public String editNotice(@PathVariable(required = false) Integer noticeId, Model model) {
        Notice notice = new Notice();
        if (noticeId != null) {
            notice = noticeService.findById(noticeId).orElse(new Notice());
        }
        model.addAttribute("notice", notice);
        return "admin/editNotice";
    }

}
