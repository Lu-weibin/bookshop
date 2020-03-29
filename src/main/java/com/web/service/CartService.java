package com.web.service;

import com.base.BaseService;
import com.web.pojo.Book;
import com.web.pojo.Cart;

import java.util.List;

/**
 * @author luwb
 * @date 2020-02-29
 */
public interface CartService extends BaseService<Cart,Integer>{

    List<Book> findAllByUserid(int userid);

}
