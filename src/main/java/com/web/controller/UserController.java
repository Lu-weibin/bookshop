package com.web.controller;

import com.base.Result;
import com.base.StatusCode;
import com.base.TestResult;
import com.web.pojo.User;
import com.web.service.UserService;
import com.web.util.JwtUtil;
import com.web.util.MailUtil;
import com.web.util.ShaUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/***
 * @author luwb
 * @date 2020/02/25
 */
@CrossOrigin
@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final HttpServletRequest request;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil, HttpServletRequest request) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.request = request;
    }

    /**
     * 注册
     */
    @PostMapping("register")
    public Result register(@RequestBody User user) {
        User user1 = userService.findOneByUsername(user.getUsername(),1);
        if (user1 != null) {
            return new Result(false, StatusCode.ERROR, "用户名已存在！");
        }
        User user2 = userService.findOneByEmail(user.getEmail(), 1);
        if (user2 != null) {
            return new Result(false, StatusCode.ERROR, "该账户已存在！");
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
        new MailUtil(user.getEmail(),code).run();
        return new Result(true,StatusCode.OK,"注册成功");
    }

    @GetMapping("activation")
    public String activation(String code) {
        User user = userService.findOneByCode(code);
        if (user == null) {
            return "账户信息不存在！";
        }
        // 激活后状态为1。即正常
        user.setState(1);
        userService.save(user);
        return "激活成功";
    }

    /**
     * 登录
     */
    @PostMapping("login")
    public Result findOneByUsername(@RequestBody User user) {
        user.setUserType(1);
        if (user.getEmail() != null && user.getPassword() != null) {
             User user1 = userService.findOneByEmailAndPassword(user);
            if (user1 != null) {
                if (user1.getState() == 3) {
                    return new Result(false, StatusCode.ERROR,"请到邮箱中激活账户！");
                }
                String role = user.getUserType() == 1 ? "user" : "admin";
                String token = jwtUtil.createJwt(user1.getId().toString(), user1.getUsername(), role);
                Map<String, String> map = new HashMap<>(16);
                map.put("token", token);
                map.put("email", user1.getEmail());
                map.put("username", user1.getUsername());
                // 头像固定不变
//                map.put("avatar","http://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
                return new Result(map);
            }
        }
        return new Result(false, StatusCode.ERROR, "用户名或密码错误！");
    }

    @GetMapping("info")
    public TestResult getInfo() {
        String roles = "roles";
        String token = request.getHeader("bookshop_token");
		if (token!=null) {
			Claims claims = jwtUtil.parseJwt(token);
			if (claims != null) {
				String adminRoles = "admin";
				if (adminRoles.equals(claims.get(roles))) {
					Map<String, Object> map = new HashMap<>(16);
					map.put("roles", new String[]{"admin"});
					map.put("name", claims.getSubject());
					map.put("avatar", "http://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
					return new TestResult(map);
				}
			}
		}
        return new TestResult(StatusCode.ACCESSERROR, "token失效");
    }

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

    @RequestMapping("logout")
    public TestResult logout() {
        return new TestResult();
    }


}
