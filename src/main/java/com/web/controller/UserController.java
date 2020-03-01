package com.web.controller;

import com.base.Result;
import com.base.StatusCode;
import com.web.pojo.User;
import com.web.repository.UserRepository;
import com.web.service.UserService;
import com.web.util.JwtUtil;
import com.web.util.ShaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Optional;

/***
 * @author luwb
 * @date 2020/02/25
 */
@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 注册
     */
    @PostMapping("register")
    public Result register(@RequestBody User user) {
        User user1 = userService.findOneByUsername(user.getUsername());
        if (user1 != null) {
            return new Result(false, StatusCode.ERROR, "用户名已存在");
        }
        user.setId(null);
        user.setPassword(ShaUtils.encrypt(user.getPassword()));
        user.setCreateTime(new Timestamp(System.currentTimeMillis()));
        userService.save(user);
        return new Result("注册成功");
    }

    /**
     * 登录
     */
//    @PostMapping("login")
    public Result findOneByUsername(@RequestBody User user) {
        User user1 = userService.findOneByUsernameAndPassword(user);
        if (user1 != null) {
            String token = jwtUtil.createJWT(user1.getId().toString(), user1.getUsername(), "user");
            return new Result(token);
        }
        return new Result(false, StatusCode.ERROR, "用户名或密码错误！");
    }

    @Autowired
    UserRepository userRepository;

    @GetMapping("{id}")
    public Result findOne(@PathVariable Integer id) {
        Optional<User> optional = userService.findById(id);
        if (optional.isPresent()) {
            User user = optional.get();
            return new Result(user);
        }
		return new Result(false,StatusCode.ERROR,"id不存在");
    }

    @GetMapping
    public Result list() {
        return new Result(userService.findAll());
    }

    @DeleteMapping("{id}")
    public Result delete(@PathVariable Integer id) {
        return new Result(userService.findById(id));
    }

}
