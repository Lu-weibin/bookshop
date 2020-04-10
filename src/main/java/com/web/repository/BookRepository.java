package com.web.repository;

import com.base.JpaBaseRepository;
import com.web.pojo.Book;
import com.web.pojo.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author luwb
 * @date 2020/02/25
 */
public interface BookRepository extends JpaBaseRepository<Book, Integer> {

    List<Book> findAllByState(int state);

    List<Book> findAllByCategory(Category category);

    @Query(value = "select b.* from book b,cart c where  c.bookid = b.id and c.userid = :userid and c.state = 1",nativeQuery = true)
    List<Book> findBooksByCart(@Param("userid") int userid);

    @Query(value = "SELECT coalesce(sum(price),0) from book WHERE state=1 and id in :ids",nativeQuery = true)
    BigDecimal totalPriceByBookids(@Param("ids") Integer[] ids);

}
