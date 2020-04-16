package com.web.repository;

import com.base.JpaBaseRepository;
import com.web.pojo.Cart;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author luwb
 * @date 2020-02-29
 */
public interface CartRepository extends JpaBaseRepository<Cart,Integer>{

    List<Cart> findAllByUserId(int userId);

    Cart findFirstByUserIdAndBookIdAndState(int userId, int bookId, int state);

    @Modifying
    @Query(value = "DELETE from cart where user_id = :userId and book_id = :bookId and state = 1;",nativeQuery = true)
    void deleteCart(@Param("userId") int userId, @Param("bookId") int bookId);

    @Modifying
    @Query(value = "update cart set state=:state where user_id = :userId and book_id in :bookIds", nativeQuery = true)
    void updateCartState(@Param("userId") int userId, @Param("bookIds") Integer[] bookIds, @Param("state") int state);

}
