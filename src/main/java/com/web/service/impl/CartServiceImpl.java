package com.web.service.impl;

import com.base.BaseServiceImpl;
import com.base.JpaBaseRepository;
import com.web.pojo.Book;
import com.web.pojo.Cart;
import com.web.repository.BookRepository;
import com.web.repository.CartRepository;
import com.web.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * @author luwb
 * @date 2020-02-29
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CartServiceImpl extends BaseServiceImpl<Cart,Integer> implements CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private BookRepository bookRepository;

    @Override
    public JpaBaseRepository<Cart, Integer> getRepository() {
        return this.cartRepository;
    }

    @Override
    public List<Book> findAllByUserId(int userId) {
        return bookRepository.findBooksByCart(userId);
    }

    @Override
    public Cart findByUserIdAndBookIdAndState(int userId, int bookId, int state) {
        return cartRepository.findFirstByUserIdAndBookIdAndState(userId, bookId, state);
    }

    @Override
    public boolean deleteCart(int userId, int bookId, int state) {
        try {
            cartRepository.deleteCart(userId,bookId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateCartState(int userId, Integer[] bookIds, int state) {
        try {
            cartRepository.updateCartState(userId, bookIds, state);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
