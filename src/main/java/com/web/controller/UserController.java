package com.web.controller;

import com.base.Result;
import com.web.pojo.User;
import com.web.service.UserService;
import com.web.util.MailUtil;
import com.web.util.ShaUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin
@Controller
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final HttpServletRequest request;

    public UserController(UserService userService, HttpServletRequest request) {
        this.userService = userService;
        this.request = request;
    }

    /**
     * 注册
     */
    @PostMapping("register")
    @ResponseBody
    public Result register(@RequestBody User user) {
        User user1 = userService.findOneByUsername(user.getUsername(), 1);
        if (user1 != null) {
            return new Result(false, "用户名已存在！");
        }
        User user2 = userService.findOneByEmail(user.getEmail(), 1);
        if (user2 != null) {
            return new Result(false, "该账户已存在！");
        }
        user.setId(null);
        user.setPassword(ShaUtils.encrypt(user.getPassword()));
        user.setCreateTime(new Timestamp(System.currentTimeMillis()));
        // 用户类型。1为普通用户；2为管理员
        user.setUserType(1);
        // 刚注册时状态为3。即未激活
        user.setState(3);
        // 账户激活码
        String code = UUID.randomUUID().toString();
        user.setCode(code);
        userService.save(user);
        new MailUtil(user.getEmail(), code).run();
        return new Result(true, "注册成功");
    }

    @GetMapping("activation")
    @ResponseBody
    public Result activation(String code) {
        User user = userService.findOneByCode(code);
        if (user == null) {
            return new Result(false, "账户信息不存在！");
        }
        // 激活后状态为1。即正常
        user.setState(1);
        userService.save(user);
        return new Result(true, "激活成功!");
    }

    /**
     * 登录
     */
    @PostMapping("login")
    public String findOneByUsername(String email, String password, String checkImg, Integer userType, RedirectAttributes redirectAttributes) {
        // 忽略验证码大小写
        redirectAttributes.addFlashAttribute("email", email);
        redirectAttributes.addFlashAttribute("password", password);
        if (userType == 1 && !(checkImg.equalsIgnoreCase(request.getSession().getAttribute("code").toString()))) {
            redirectAttributes.addFlashAttribute("error", "验证码不正确！");
            return "redirect:../login";
        }
        User user;
        String loginPage = userType == 1 ? "redirect:../login" : "redirect:../admin/login";
        if (email != null && password != null) {
            user = userService.findOneByEmailAndPassword(email, password, userType);
            if (user == null) {
                redirectAttributes.addFlashAttribute("error", "用户名或密码错误！");
                return loginPage;
            }
            if (user.getState() == 3) {
                redirectAttributes.addFlashAttribute("error", "请到邮箱中激活账户！");
                return loginPage;
            }
            if (user.getState() == 2) {
                redirectAttributes.addFlashAttribute("error", "您的账号状态异常！");
                return loginPage;
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "信息不能为空！");
            return loginPage;
        }
        String id = userType == 1 ? "userId" : "adminId";
        String name = userType == 1 ? "username" : "adminName";
        request.getSession().setAttribute(id, user.getId());
        request.getSession().setAttribute(name, user.getUsername());
        return userType == 1 ? "redirect:../index" : "redirect:../admin/welcome";
    }

    @GetMapping("id")
    @ResponseBody
    public Result findOne() {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        return new Result(userService.findById(userId).orElse(new User()));
    }

    @PostMapping("update/{username}/{phone}")
    @ResponseBody
    public Result updateUserInfo(@PathVariable String username, @PathVariable String phone) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        User user1 = userService.findOneByUsername(username, 1);
        if (user1 != null && !user1.getId().equals(userId)) {
            return new Result(false, "用户名已存在！");
        }
        Optional<User> optional = userService.findById(userId);
        if (optional.isPresent()) {
            User user = optional.get();
            user.setUsername(username);
            user.setPhone(phone);
            userService.save(user);
            request.getSession().setAttribute("username", username);
            return new Result(true,"更新成功！");
        }
        return new Result(false, "更新失败！");
    }

    @PostMapping("changePassword/{oldPassword}/{newPassword}")
    @ResponseBody
    public Result changePassword(@PathVariable String oldPassword, @PathVariable String newPassword) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        User user = userService.findById(userId).orElse(new User());
        if (!user.getPassword().equals(ShaUtils.encrypt(oldPassword))) {
            return new Result(false, "原密码错误！");
        } else {
            user.setPassword(ShaUtils.encrypt(newPassword));
            userService.save(user);
            return new Result(true,"修改成功！");
        }
    }

    @GetMapping("list")
    @ResponseBody
    public Result list() {
        return new Result(userService.findAllUser());
    }

    @PostMapping("state/{id}/{state}")
    @ResponseBody
    public Result updateUserByState(@PathVariable Integer id, @PathVariable int state) {
        User user = userService.findById(id).orElse(new User());
        user.setState(state);
        userService.save(user);
        return new Result(true,"执行成功");
    }

    @GetMapping("search")
    @ResponseBody
    public Result search(@RequestParam String email, @RequestParam String username, @RequestParam String phone, @RequestParam Integer state) {
        List<User> users = userService.search(email, username, phone, state);
        return new Result(users);
    }


}
