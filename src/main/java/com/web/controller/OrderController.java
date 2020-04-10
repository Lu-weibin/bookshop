package com.web.controller;

import com.base.Result;
import com.base.StatusCode;
import com.web.pojo.Orders;
import com.web.service.BookService;
import com.web.service.CartService;
import com.web.service.OrderService;
import com.web.util.CommonUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

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
    private BookService bookService;
    @Autowired
    private CartService cartService;
    @Autowired
    private HttpServletRequest request;

    @GetMapping(value = "state/{state}")
    public Result getOrders(@PathVariable Integer state) {
        Integer userid = (Integer) request.getSession().getAttribute("userid");
        return new Result(orderService.findAllByUserid(userid, state));
    }

    @PostMapping(value = "add")
    public Result addOrder(@RequestBody Map<String,Integer[]> bookids) {
        Integer userid = (Integer) request.getSession().getAttribute("userid");
        String key = "bookids";
        Integer[] integers = bookids.get(key);
        Orders orders = new Orders();
        orders.setOrderNumber(CommonUtil.getOrderNumber());
        orders.setUserid(userid);
        orders.setAddress("广东潮州");
        orders.setCreateTime(new Timestamp(System.currentTimeMillis()));
        orders.setState(1);
        orders.setPhone("13106658600");
        // 根据bookids计算金额
        orders.setPayPrice(bookService.totalPriceByBookids(integers));
        orderService.save(orders);
        // 结算后要把购物车的商品更改为结算，即state为2
        cartService.updateCartState(userid, integers, 2);
        return new Result(orderService.save(orders));
    }

}
