package com.web.repository;

import com.base.JpaBaseRepository;
import com.web.pojo.Book;

import java.util.List;

/**
 * @author luwb
 * @date 2020/02/25
 */
public interface BookRepository extends JpaBaseRepository<Book, Integer> {

    List<Book> findAllByState(int state);

    List<Book> findByBookNameContainsOrAuthorContains(String key,String key2);

//    List<Book> findAllByBookNameLikeOrAuthorLike(String key);

}
