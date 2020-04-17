package com.web.service;

import com.base.BaseService;
import com.web.pojo.Book;
import com.web.pojo.Cart;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface CartService extends BaseService<Cart, Integer> {

    List<Book> findAllByUserId(int userId);

    Cart findByUserIdAndBookIdAndState(int userId, int bookId, int state);

    boolean deleteCart(int userId, int bookId, int state);

    boolean updateCartState(int userId, Integer[] bookIds, int state);
}
