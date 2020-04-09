package com.web.repository;

import com.base.JpaBaseRepository;
import com.web.pojo.Book;
import com.web.pojo.Cart;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author luwb
 * @date 2020-02-29
 */
public interface CartRepository extends JpaBaseRepository<Cart,Integer>{

    List<Cart> findAllByUserid(int userid);

    Cart findFirstByUseridAndBookidAndState(int userid, int bookid, int state);

    @Modifying
    @Query(value = "DELETE from cart where userid = :userid and bookid = :bookid and state = 1;",nativeQuery = true)
    void deleteCart(@Param("userid") int userid, @Param("bookid") int bookid);


}
