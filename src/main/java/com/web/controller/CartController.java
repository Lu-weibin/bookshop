package com.web.controller;

import com.base.Result;
import com.base.StatusCode;
import com.web.pojo.Cart;
import com.web.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * @author luwb
 * @date 2020-02-29
 */
@RestController
@RequestMapping("cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public Result list() {
        return new Result(cartService.findAll());
    }

    @PostMapping("book/{bookid}")
    public Result save(@PathVariable int bookid) {
        Cart cart = new Cart();
        // todo 获取用户id
        cart.setBookid(bookid);
        cart.setBookCount(1);
        cart.setUserid(1);
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

    @DeleteMapping("{id}")
    public Result delete(@PathVariable int id) {
        cartService.delete(id);
        return new Result("删除成功");
    }
}
