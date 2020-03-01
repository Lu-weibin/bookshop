package com.web.controller;

import com.base.Result;
import com.web.service.OrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luwb
 * @date 2020-02-29
 */
@RestController
@RequestMapping("orderDetails")
public class OrderDetailsController {
    @Autowired
    private OrderDetailsService orderDetailsService;

    @GetMapping
    public Result list() {
        return new Result(orderDetailsService.findAll());
    }
}
