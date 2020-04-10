package com.web.controller;

import com.base.Result;
import com.base.StatusCode;
import com.web.pojo.Address;
import com.web.pojo.Orders;
import com.web.pojo.User;
import com.web.service.*;
import com.web.util.CommonUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private UserService userService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private HttpServletRequest request;


    @GetMapping(value = "state/{state}")
    public Result getOrders(@PathVariable Integer state) {
        Integer userid = (Integer) request.getSession().getAttribute("userid");
        return new Result(orderService.findAllByUserid(userid, state));
    }

    @PostMapping(value = "getPriceAndPhoneAndAddress")
    public Result getPriceAndPhoneAndAddress(@RequestBody Map<String, Integer[]> bookids) {
        Integer userid = (Integer) request.getSession().getAttribute("userid");
        String key = "bookids";
        Integer[] integers = bookids.get(key);
        // 计算订单金额
        BigDecimal payPrice = bookService.totalPriceByBookids(integers);
        Map<String, Object> map = new HashMap<>(16);
        Optional<User> optional = userService.findById(userid);
        Address address = addressService.findDefaultAddress(userid);
        map.put("payPrice", payPrice);
        if (optional.isPresent()) {
            User user = optional.get();
            map.put("phone", user.getPhone());
            map.put("address", address.getAddress());
        }
        return new Result(map);
    }

    @PostMapping(value = "add")
    public Result addOrder(@RequestBody Map<String,Object> map) {
        Integer userid = (Integer) request.getSession().getAttribute("userid");
        Integer[] bookids = (Integer[]) ((List)map.get("bookids")).toArray(new Integer[0]);
        BigDecimal payPrice = new BigDecimal(map.get("payPrice").toString());
        String phone = (String) map.get("phone");
        String address = (String) map.get("selectAddress");
        Orders orders = new Orders();
        orders.setOrderNumber(CommonUtil.getOrderNumber());
        orders.setUserid(userid);
        orders.setAddress(address);
        orders.setCreateTime(new Timestamp(System.currentTimeMillis()));
        orders.setPayTime(new Timestamp(System.currentTimeMillis()));
        orders.setState(2);
        orders.setPhone(phone);
        orders.setPayPrice(payPrice);
        orderService.save(orders);
        // 结算后要把购物车的商品更改为结算，即state为2
        cartService.updateCartState(userid, bookids, 2);
        return new Result(orderService.save(orders));
    }

}
