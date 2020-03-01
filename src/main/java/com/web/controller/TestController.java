package com.web.controller;

import com.base.TestResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luwb
 * @date 2020-03-01
 */
@CrossOrigin
@RestController
public class TestController {

    @PostMapping(value = "/user/login")
    public TestResult test1() {
        Map<String, String> map = new HashMap<>();
        map.put("token", "admin");
        return new TestResult(map);
    }

    @GetMapping("user/info")
    public TestResult test2() {
        Map<String, Object> map = new HashMap<>();
        map.put("roles", new String[]{"admin"});
        map.put("name", "admin");
        // https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif?imageView2/1/w/80/h/80
        map.put("avatar", "http://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return new TestResult(map);
    }

    @RequestMapping("user/logout")
    public TestResult test3() {
        return new TestResult();
    }

}
