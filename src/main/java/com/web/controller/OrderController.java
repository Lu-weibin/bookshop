package com.web.controller;

import com.base.Result;
import com.web.pojo.*;
import com.web.service.*;
import com.web.util.CommonUtil;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("order")
@CrossOrigin
public class OrderController {

    private final OrderService orderService;
    private final BookService bookService;
    private final CartService cartService;
    private final UserService userService;
    private final AddressService addressService;
    private final OrderDetailsService orderDetailsService;
    private final HttpServletRequest request;

    public OrderController(OrderService orderService, BookService bookService, CartService cartService, UserService userService, AddressService addressService, OrderDetailsService orderDetailsService, HttpServletRequest request) {
        this.orderService = orderService;
        this.bookService = bookService;
        this.cartService = cartService;
        this.userService = userService;
        this.addressService = addressService;
        this.orderDetailsService = orderDetailsService;
        this.request = request;
    }

    @GetMapping(value = "state/{state}")
    public Result getOrders(@PathVariable Integer state) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        return new Result(orderService.findAllByUserId(userId, state));
    }

    @PostMapping(value = "getPriceAndPhoneAndAddress")
    public Result getPriceAndPhoneAndAddress(@RequestBody Map<String, Integer[]> bookIds) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        String key = "bookIds";
        Integer[] integers = bookIds.get(key);
        // 计算订单金额
        BigDecimal payPrice = bookService.totalPriceByBookIds(integers);
        Map<String, Object> map = new HashMap<>(16);
        Optional<User> optional = userService.findById(userId);
        Address address = addressService.findDefaultAddress(userId);
        map.put("payPrice", payPrice);
        if (optional.isPresent()) {
            User user = optional.get();
            map.put("phone", user.getPhone());
            map.put("address", address == null ? "" : address.getAddress());
        }
        return new Result(map);
    }

    @PostMapping(value = "add")
    public Result addOrder(@RequestBody Map<String, Object> map) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        Integer[] bookIds = (Integer[]) ((List) map.get("bookIds")).toArray(new Integer[0]);
        BigDecimal payPrice = new BigDecimal(map.get("payPrice").toString());
        String addressee = (String) map.get("addressee");
        String phone = (String) map.get("phone");
        String address = (String) map.get("selectAddress");
        Integer state = (Integer) map.get("state");
        if ("".equals(address.trim())) {
            return new Result(false, "请到个人中心添加地址！");
        }
        // 创建订单前先检查图书状态，图书状态不为出售时 订单创建失败
        if (checkBooks(bookIds)) {
            // 保存订单
            Orders orders = saveOrder(userId, addressee, address, state, phone, payPrice);
            orderService.save(orders);
            // 保存订单详情
            saveOrderDetails(orders, bookIds, state);
            // 结算后要把购物车的商品更改为结算，即state为2
            cartService.updateCartState(userId, bookIds, 2);
            // 支付后要把图书状态改变。图书状态3 表示已卖出。还要将该图书分类下图书数量-1
            if (state == 2) {
                bookService.updateState(bookIds, 3);
            }
            return new Result(true, "支付成功！");
        }
        return new Result(false, "支付失败，请刷新页面后重新支付！");
    }

    @GetMapping("orderDetails/{orderId}")
    public Result getOrderDetails(@PathVariable int orderId) {
        Optional<Orders> optional = orderService.findById(orderId);
        Map<String, Object> map = new HashMap<>(16);
        if (optional.isPresent()) {
            Orders orders = optional.get();
            map.put("order", orders);
        } else {
            return new Result(false, "订单不存在！");
        }
        List<OrderDetails> orderDetails = orderDetailsService.findAllByOrderId(orderId);
        List<Integer> bookIds = new ArrayList<>();
        for (OrderDetails orderDetail : orderDetails) {
            bookIds.add(orderDetail.getBookId());
        }
        List<Book> books = bookService.findAllByIds(bookIds.toArray(new Integer[0]));
        map.put("books", books);
        return new Result(map);
    }

    @PostMapping("update/state/{orderId}/{state}")
    public Result updateState(@PathVariable int orderId, @PathVariable int state) {
        Optional<Orders> optional = orderService.findById(orderId);
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
            List<OrderDetails> details = orderDetailsService.findAllByOrderId(orderId);
            List<Integer> bookIds = new ArrayList<>();
            for (OrderDetails detail : details) {
                OrderDetails orderDetails = orderDetailsService.findById(detail.getId()).get();
                bookIds.add(orderDetails.getBookId());
                orderDetails.setState(state);
                orderDetailsService.save(orderDetails);
            }
            // 支付后要把图书状态改变。图书状态3 表示已卖出
            if (state == 2) {
                bookService.updateState(bookIds.toArray(new Integer[0]), 3);
            }
        }
        return state == 2 ? new Result(true, "支付成功") : new Result(false, "订单超过30分钟，已失效！");
    }

    private Orders saveOrder(int userId, String addressee, String address, int state, String phone, BigDecimal payPrice) {
        Orders orders = new Orders();
        orders.setOrderNumber(CommonUtil.getOrderNumber());
        orders.setUserId(userId);
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

    private void saveOrderDetails(Orders orders, Integer[] bookIds, int state) {
        for (Integer bookId : bookIds) {
            // 保存订单详情
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrderId(orders.getId());
            orderDetails.setBookId(bookId);
            orderDetails.setBookCount(1);
            orderDetails.setCreateTime(CommonUtil.now());
            orderDetails.setBookPrice(bookService.findById(bookId).orElse(new Book()).getPrice());
            orderDetails.setState(state);
            orderDetailsService.save(orderDetails);
        }
    }

    private boolean checkBooks(Integer[] bookIds) {
        for (Integer bookId : bookIds) {
            Optional<Book> optional = bookService.findById(bookId);
            if (optional.isPresent()) {
                Book book = optional.get();
                if (book.getState() != 2) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }
}
