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

    List<Book> findAllByUserid(Integer userid);

    boolean deleteCollection(Integer userid, int bookid);

    Collection findByUseridAndBookid(Integer userid, int bookid);
}
