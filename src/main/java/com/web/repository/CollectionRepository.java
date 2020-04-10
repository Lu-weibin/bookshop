package com.web.repository;

import com.base.JpaBaseRepository;
import com.web.pojo.Book;
import com.web.pojo.Collection;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author luwb
 * @date 2020-02-29
 */
public interface CollectionRepository extends JpaBaseRepository<Collection,Integer>{

    int deleteByUseridAndBookid(int userid, int bookid);

    Collection findFirstByUseridAndBookid(int userid, int bookid);

}
