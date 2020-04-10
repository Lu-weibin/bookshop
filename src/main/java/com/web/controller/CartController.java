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
        Integer userid = (Integer) request.getSession().getAttribute("userid");
        return new Result(cartService.findAllByUserid(userid));
    }

    /**
     * 加入购物车
     */
    @PostMapping("book/{bookid}")
    public Result save(@PathVariable int bookid) {
        Integer userid = (Integer) request.getSession().getAttribute("userid");
        if (userid == null) {
            return new Result(false, StatusCode.LOGINERROR, "未登录");
        }
        // 校验该用户是否已加入过该书籍
        Cart existCart = cartService.findByUseridAndBookidAndState(userid, bookid, 1);
        if (existCart != null) {
            return new Result(false, StatusCode.LOGINERROR, "已加入,不要重复添加！");
        }
        Cart cart = new Cart();
        cart.setBookid(bookid);
        cart.setBookCount(1);
        cart.setUserid(userid);
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
    @GetMapping("delete/{bookid}")
    public Result delete(@PathVariable int bookid) {
        Integer userid = (Integer) request.getSession().getAttribute("userid");
        if (userid == null) {
            return new Result(false, StatusCode.LOGINERROR, "未登录");
        }
        if (cartService.deleteCart(userid, bookid, 1)) {
            return new Result("删除成功");
        }
        return new Result(false, StatusCode.ERROR, "删除失败");
    }
}
