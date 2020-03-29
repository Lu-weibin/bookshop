package com.web.repository;

import com.base.JpaBaseRepository;
import com.web.pojo.Book;
import com.web.pojo.Cart;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author luwb
 * @date 2020-02-29
 */
public interface CartRepository extends JpaBaseRepository<Cart,Integer>{

    List<Cart> findAllByUserid(int userid);


}
