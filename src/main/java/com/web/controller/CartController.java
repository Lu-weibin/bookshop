package com.web.controller;

import com.base.Result;
import com.base.StatusCode;
import com.web.pojo.Cart;
import com.web.service.CartService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Optional;

/**
 * @author luwb
 * @date 2020-02-29
 */
@RestController
@RequestMapping("cart")
@CrossOrigin
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("list")
    public Result list() {
        Claims userClaims = (Claims) request.getAttribute("user_claims");
        if (userClaims != null) {
            String userid = userClaims.getId();
            return new Result(cartService.findAllByUserid(Integer.parseInt(userid)));
        } else {
            return new Result(false, StatusCode.ERROR, "未登录");
        }
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
