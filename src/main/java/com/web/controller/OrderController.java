package com.web.controller;

import com.base.Result;
import com.base.StatusCode;
import com.web.service.OrderService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author luwb
 * @date 2020-02-29
 */
@RestController
@RequestMapping("order")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpServletRequest request;

    @GetMapping(value = "state/{state}")
    public Result list(@PathVariable Integer state) {
        Claims userClaims = (Claims) request.getAttribute("user_claims");
        if (userClaims != null) {
            String userid = userClaims.getId();
            return new Result(orderService.findAllByUserid(Integer.parseInt(userid),state));
        } else {
            return new Result(false, StatusCode.ERROR, "未登录");
        }
    }

}
