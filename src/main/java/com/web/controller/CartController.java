package com.web.controller;

import com.base.Result;
import com.base.StatusCode;
import com.web.pojo.Book;
import com.web.pojo.Cart;
import com.web.service.BookService;
import com.web.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Optional;

@RestController
@RequestMapping("cart")
@CrossOrigin
public class CartController {

    private final CartService cartService;
    private final BookService bookService;
    private final HttpServletRequest request;

    public CartController(CartService cartService, BookService bookService, HttpServletRequest request) {
        this.cartService = cartService;
        this.bookService = bookService;
        this.request = request;
    }

    @GetMapping("list")
    public Result list() {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        return new Result(cartService.findAllByUserId(userId));
    }

    /**
     * 加入购物车
     */
    @PostMapping("book/{bookId}")
    public Result save(@PathVariable int bookId) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if (userId == null) {
            return new Result(false, StatusCode.LOGINERROR, "未登录");
        }
        // 加入购物车前先判断是否是自己的图书，是的话不让加入
        Book book = bookService.findById(bookId).orElse(null);
        assert book != null;
        if (book.getUser().getId().equals(userId)) {
            return new Result(false, StatusCode.ERROR, "不要加入自己的图书！");
        }
        // 校验该用户是否已加入过该书籍
        Cart existCart = cartService.findByUserIdAndBookIdAndState(userId, bookId, 1);
        if (existCart != null) {
            return new Result(false, StatusCode.LOGINERROR, "已加入,不要重复添加！");
        }
        Cart cart = new Cart();
        cart.setBookId(bookId);
        cart.setBookCount(1);
        cart.setUserId(userId);
        // 1为加入的状态；2为结算
        cart.setState(1);
        cart.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return new Result(cartService.save(cart));
    }

    @PostMapping("{id}/{state}")
    public Result updateState(@PathVariable int id,@PathVariable int state) {
        Optional<Cart> optional = cartService.findById(id);
        if (optional.isPresent()) {
            Cart cart = optional.get();
            cart.setState(state);
            return new Result(cartService.save(cart));
        }
        return new Result(false, StatusCode.ERROR,"操作失败");
    }

    /**
     * 删除购物车
     */
    @GetMapping("delete/{bookId}")
    public Result delete(@PathVariable int bookId) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if (userId == null) {
            return new Result(false, StatusCode.LOGINERROR, "未登录");
        }
        if (cartService.deleteCart(userId, bookId, 1)) {
            return new Result("删除成功");
        }
        return new Result(false, StatusCode.ERROR, "删除失败");
    }
}
