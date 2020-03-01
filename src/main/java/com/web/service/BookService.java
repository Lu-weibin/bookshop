package com.web.service;

import com.base.BaseService;
import com.web.pojo.Book;

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
    List<Book> findAllByBookNameLikeOrAuthorLike(String key);

}
