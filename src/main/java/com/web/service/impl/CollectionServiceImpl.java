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


@Service
@Transactional(rollbackFor = Exception.class)
public class CollectionServiceImpl extends BaseServiceImpl<Collection,Integer> implements CollectionService{

    private final CollectionRepository collectionRepository;
    private final BookRepository bookRepository;

    public CollectionServiceImpl(CollectionRepository collectionRepository, BookRepository bookRepository) {
        this.collectionRepository = collectionRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public JpaBaseRepository<Collection, Integer> getRepository() {
        return this.collectionRepository;
    }

    @Override
    public List<Book> findAllByUserId(Integer userId) {
        return bookRepository.findCollectionByUserId(userId);
    }

    @Override
    public boolean deleteCollection(Integer userId, int bookId) {
        try {
            collectionRepository.deleteByUserIdAndBookId(userId, bookId);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Collection findByUserIdAndBookId(Integer userId, int bookId) {
        return collectionRepository.findFirstByUserIdAndBookId(userId, bookId);
    }

}
