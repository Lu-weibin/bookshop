package com.web.service.impl;

import com.base.BaseServiceImpl;
import com.base.JpaBaseRepository;
import com.web.pojo.Cart;
import com.web.repository.CartRepository;
import com.web.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author luwb
 * @date 2020-02-29
 */
@Service
public class CartServiceImpl extends BaseServiceImpl<Cart,Integer> implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Override
    public JpaBaseRepository<Cart, Integer> getRepository() {
        return this.cartRepository;
    }
}
