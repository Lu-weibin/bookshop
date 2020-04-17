package com.web.repository;

import com.base.JpaBaseRepository;
import com.web.pojo.Book;
import com.web.pojo.Collection;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CollectionRepository extends JpaBaseRepository<Collection,Integer>{

    int deleteByUserIdAndBookId(int userId, int bookId);

    Collection findFirstByUserIdAndBookId(int userId, int bookId);

}
