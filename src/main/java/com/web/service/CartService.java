package com.web.service;

import com.base.BaseService;
import com.web.pojo.Book;
import com.web.pojo.Cart;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author luwb
 * @date 2020-02-29
 */
public interface CartService extends BaseService<Cart, Integer> {

    List<Book> findAllByUserid(int userid);

    Cart findByUseridAndBookidAndState(int userid, int bookid, int state);

    boolean deleteCart(int userid, int bookid, int state);

    boolean updateCartState(int userid, Integer[] bookids, int state);
}
