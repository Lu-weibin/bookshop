package com.web.service;

import com.base.BaseService;
import com.web.pojo.Book;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface BookService extends BaseService<Book,Integer> {

    /**
     * 根据图书状态查询列表
     */
    List<Book> findAllByState(int state);

    /**
     * 根据id数组查询图书列表
     */
    List<Book> findAllByIds(Integer[] ids);

    /**
     * 更新图书状态
     */
    Book update(int id, int state);

    List<Book> findAllByCategoryId(Integer categoryId);

    /**
     * 根据多个图书id计算总金额
     */
    BigDecimal totalPriceByBookIds(Integer[] bookIds);

    boolean updateState(Integer[] bookIds, int state);

    List<Book> findAllByUserId(Integer userId);

    List<Book> searchByKey(String key);

    List<Book> search(String bookName, String author, String publisher, Integer categoryId, Integer state);
}
