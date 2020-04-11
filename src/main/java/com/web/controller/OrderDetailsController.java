package com.web.controller;

import com.base.Result;
import com.base.StatusCode;
import com.web.pojo.Book;
import com.web.pojo.OrderDetails;
import com.web.service.BookService;
import com.web.service.OrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author luwb
 * @date 2020-02-29
 */
@RestController
@RequestMapping("orderDetails")
@CrossOrigin
public class OrderDetailsController {

    @Autowired
    private OrderDetailsService orderDetailsService;
    @Autowired
    private BookService bookService;

    @PostMapping("update/state/{orderid}/{bookid}/{state}")
    public Result updateState(@PathVariable int orderid, @PathVariable int bookid,@PathVariable int state) {
        OrderDetails orderDetails = orderDetailsService.findOneByOrderidAndBookid(orderid, bookid);
        if (orderDetails != null) {
            // 订单详情 状态4为退货
            orderDetails.setState(4);
            Book book = bookService.findById(bookid).orElse(null);
            assert book != null;
            book.setState(4);
            bookService.save(book);
            return new Result(orderDetailsService.save(orderDetails));
        }
        return new Result(false, StatusCode.ERROR, "订单信息不存在！");

    }


}
