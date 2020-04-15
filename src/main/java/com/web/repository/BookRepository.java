package com.web.repository;

import com.base.JpaBaseRepository;
import com.web.pojo.Book;
import com.web.pojo.Category;
import com.web.pojo.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author luwb
 * @date 2020/02/25
 */
public interface BookRepository extends JpaBaseRepository<Book, Integer> {

    List<Book> findAllByState(int state, Sort sort);

    List<Book> findAllByCategoryAndState(Category category, int state, Sort sort);

    List<Book> findAllByIdIn(Integer[] ids);

    @Query(value = "select b.* from book b,cart c where  c.bookid = b.id and c.userid = :userid and c.state = 1",nativeQuery = true)
    List<Book> findBooksByCart(@Param("userid") int userid);

    @Query(value = "SELECT coalesce(sum(price),0) from book WHERE id in :ids",nativeQuery = true)
    BigDecimal totalPriceByBookids(@Param("ids") Integer[] ids);

    @Query(value = "select * from book t1,collection t2 WHERE t1.id = t2.bookid and t2.userid = :userid ",nativeQuery = true)
    List<Book> findCollectionByUserid(@Param("userid") Integer userid);

    List<Book> findAllByUser(User user);

    List<Book> findAllByBookNameLikeOrAuthorLikeOrPublisherIsLike(String bookName, String author, String publisher);

}
