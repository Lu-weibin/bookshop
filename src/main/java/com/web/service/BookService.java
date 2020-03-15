package com.web.service;

import com.base.BaseService;
import com.web.pojo.Book;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author luwb
 * @date 2020/02/25
 */
public interface BookService extends BaseService<Book,Integer> {

    /**
     * 根据图书状态查询列表
     */
    List<Book> findAllByState(int state);

    /**
     * 根据书名或作者查询列表
     */
    Page<Book> findPageByBookNameLikeOrAuthorLike(int page, int size, Book book);

    /**
     * 更新图书状态
     */
    Book update(int id, int state);

    List<Book> findAllByCategoryid(Integer categoryid);
}
