package com.web.controller;

import com.base.Result;
import com.base.StatusCode;
import com.web.pojo.Book;
import com.web.pojo.OrderDetails;
import com.web.service.BookService;
import com.web.service.OrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("orderDetails")
@CrossOrigin
public class OrderDetailsController {

    private final OrderDetailsService orderDetailsService;
    private final BookService bookService;

    public OrderDetailsController(OrderDetailsService orderDetailsService, BookService bookService) {
        this.orderDetailsService = orderDetailsService;
        this.bookService = bookService;
    }

    @PostMapping("update/state/{orderId}/{bookId}/{state}")
    public Result updateState(@PathVariable int orderId, @PathVariable int bookId,@PathVariable int state) {
        OrderDetails orderDetails = orderDetailsService.findOneByOrderIdAndBookId(orderId, bookId);
        if (orderDetails != null) {
            // 订单详情 状态4为退货
            orderDetails.setState(4);
            Book book = bookService.findById(bookId).orElse(null);
            assert book != null;
            book.setState(4);
            bookService.save(book);
            return new Result(orderDetailsService.save(orderDetails));
        }
        return new Result(false, StatusCode.ERROR, "订单信息不存在！");

    }


}
