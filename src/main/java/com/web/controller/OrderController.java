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
    public Result getOrders(@PathVariable Integer state) {
        Integer userid = (Integer) request.getSession().getAttribute("userid");
        return new Result(orderService.findAllByUserid(userid, state));
    }

}
