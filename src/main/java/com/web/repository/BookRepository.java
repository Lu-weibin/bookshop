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

public interface BookRepository extends JpaBaseRepository<Book, Integer> {

    List<Book> findAllByState(int state, Sort sort);

    List<Book> findAllByCategoryAndState(Category category, int state, Sort sort);

    List<Book> findAllByIdIn(Integer[] ids);

    @Query(value = "select b.* from book b,cart c where  c.book_id = b.id and c.user_id = :userId and c.state = 1",nativeQuery = true)
    List<Book> findBooksByCart(@Param("userId") int userId);

    @Query(value = "SELECT coalesce(sum(price),0) from book WHERE id in :ids",nativeQuery = true)
    BigDecimal totalPriceByBookIds(@Param("ids") Integer[] ids);

    @Query(value = "select * from book t1,collection t2 WHERE t1.id = t2.book_id and t2.user_id = :userId ",nativeQuery = true)
    List<Book> findCollectionByUserId(@Param("userId") Integer userId);

    List<Book> findAllByUserAndStateNot(User user, Sort sort, Integer state);

    List<Book> findAllByBookNameLikeOrAuthorLikeOrPublisherIsLike(String bookName, String author, String publisher);

}
