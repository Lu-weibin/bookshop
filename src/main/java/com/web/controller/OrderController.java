package com.web.controller;

import com.base.Result;
import com.base.StatusCode;
import com.web.pojo.*;
import com.web.service.*;
import com.web.util.CommonUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

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
    private OrderDetailsService orderDetailsService;
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
            map.put("address", address == null ? "" : address.getAddress());
        }
        return new Result(map);
    }

    @PostMapping(value = "add")
    public Result addOrder(@RequestBody Map<String,Object> map) {
        Integer userid = (Integer) request.getSession().getAttribute("userid");
        Integer[] bookids = (Integer[]) ((List)map.get("bookids")).toArray(new Integer[0]);
        BigDecimal payPrice = new BigDecimal(map.get("payPrice").toString());
        String addressee = (String) map.get("addressee");
        String phone = (String) map.get("phone");
        String address = (String) map.get("selectAddress");
        Integer state = (Integer) map.get("state");
        if ("".equals(address.trim())){
            return new Result(false, StatusCode.ERROR, "请到个人中心添加地址！");
        }
        // 保存订单
        Orders orders = saveOrder(userid, addressee, address, state, phone, payPrice);
        orderService.save(orders);
        // 保存订单详情
        saveOrderDetails(orders,bookids,state);
        // 结算后要把购物车的商品更改为结算，即state为2
        cartService.updateCartState(userid, bookids, 2);
        // 支付后要把图书状态改变。图书状态3 表示已卖出
        if (state == 2) {
            bookService.updateState(bookids,3);
        }
        return new Result("支付成功");
    }

    @GetMapping("orderDetails/{orderid}")
    public Result getOrderDetails(@PathVariable int orderid) {
        Optional<Orders> optional = orderService.findById(orderid);
        Map<String, Object> map = new HashMap<>(16);
        if (optional.isPresent()) {
            Orders orders = optional.get();
            map.put("order", orders);
        } else {
            return new Result(false, StatusCode.ERROR, "订单不存在！");
        }
        List<OrderDetails> orderDetails = orderDetailsService.findAllByOrderid(orderid);
        List<Integer> bookids = new ArrayList<>();
        for (OrderDetails orderDetail : orderDetails) {
            bookids.add(orderDetail.getBookid());
        }
        List<Book> books = bookService.findAllByIds(bookids.toArray(new Integer[0]));
        map.put("books", books);
        return new Result(map);
    }

    @PostMapping("update/state/{orderid}/{state}")
    public Result updateState(@PathVariable int orderid, @PathVariable int state) {
        Optional<Orders> optional = orderService.findById(orderid);
        if (optional.isPresent()) {
            Orders orders = optional.get();
            // 判断订单创建到现在过了多久，超过半小时则订单失效
            Timestamp createTime = orders.getCreateTime();
            if (CommonUtil.getTimeDifference(CommonUtil.now(), createTime) > 30) {
                state = 3;
            } else {
              orders.setPayTime(CommonUtil.now());
            }
            orders.setState(state);
            orderService.save(orders);
            // 更新该订单详情的支付状态
            List<OrderDetails> details = orderDetailsService.findAllByOrderid(orderid);
            List<Integer> bookids = new ArrayList<>();
            for (OrderDetails detail : details) {
                OrderDetails orderDetails = orderDetailsService.findById(detail.getId()).get();
                bookids.add(orderDetails.getBookid());
                orderDetails.setState(state);
                orderDetailsService.save(orderDetails);
            }
            // 支付后要把图书状态改变。图书状态3 表示已卖出
            if (state == 2) {
                bookService.updateState(bookids.toArray(new Integer[0]),3);
            }
        }
        return state==2?new Result(true,StatusCode.OK,"支付成功"):new Result(false,StatusCode.ERROR,"订单超过30分钟，已失效！");
    }

    private Orders saveOrder(int userid, String addressee, String address, int state, String phone, BigDecimal payPrice) {
        Orders orders = new Orders();
        orders.setOrderNumber(CommonUtil.getOrderNumber());
        orders.setUserid(userid);
        orders.setAddressee(addressee);
        orders.setAddress(address);
        orders.setCreateTime(CommonUtil.now());
        if (state == 2) {
            orders.setPayTime(CommonUtil.now());
        }
        // state: 1 放弃支付；2 支付
        orders.setState(state);
        orders.setPhone(phone);
        orders.setPayPrice(payPrice);
        return orderService.save(orders);
    }

    private void saveOrderDetails(Orders orders,Integer[] bookids,int state) {
        for (Integer bookid : bookids) {
            // 保存订单详情
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrderid(orders.getId());
            orderDetails.setBookid(bookid);
            orderDetails.setBookCount(1);
            orderDetails.setCreateTime(CommonUtil.now());
            orderDetails.setBookPrice(bookService.findById(bookid).get().getPrice());
            orderDetails.setState(state);
            orderDetailsService.save(orderDetails);
        }
    }

}
