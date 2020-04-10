package com.web.service.impl;

import com.base.BaseServiceImpl;
import com.base.JpaBaseRepository;
import com.web.pojo.Book;
import com.web.pojo.Collection;
import com.web.repository.BookRepository;
import com.web.repository.CollectionRepository;
import com.web.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author luwb
 * @date 2020-02-29
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CollectionServiceImpl extends BaseServiceImpl<Collection,Integer> implements CollectionService{

    @Autowired
    private CollectionRepository collectionRepository;
    @Autowired
    private BookRepository bookRepository;

    @Override
    public JpaBaseRepository<Collection, Integer> getRepository() {
        return this.collectionRepository;
    }

    @Override
    public List<Book> findAllByUserid(Integer userid) {
        return bookRepository.findCollectionByUserid(userid);
    }

    @Override
    public boolean deleteCollection(Integer userid, int bookid) {
        try {
            collectionRepository.deleteByUseridAndBookid(userid, bookid);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Collection findByUseridAndBookid(Integer userid, int bookid) {
        return collectionRepository.findFirstByUseridAndBookid(userid, bookid);
    }

}
