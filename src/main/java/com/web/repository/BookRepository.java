package com.web.repository;

import com.base.JpaBaseRepository;
import com.web.pojo.Book;
import com.web.pojo.Category;

import java.util.List;

/**
 * @author luwb
 * @date 2020/02/25
 */
public interface BookRepository extends JpaBaseRepository<Book, Integer> {

    List<Book> findAllByState(int state);

    List<Book> findAllByCategory(Category category);

}
