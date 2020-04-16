package com.web.service;

import com.base.BaseService;
import com.web.pojo.Book;
import com.web.pojo.Collection;
import java.util.List;

/**
 * @author luwb
 * @date 2020-02-29
 */
public interface CollectionService extends BaseService<Collection,Integer> {

    List<Book> findAllByUserId(Integer userId);

    boolean deleteCollection(Integer userId, int bookId);

    Collection findByUserIdAndBookId(Integer userId, int bookId);
}
