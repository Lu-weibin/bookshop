package com.web.service.impl;

import com.base.BaseServiceImpl;
import com.base.JpaBaseRepository;
import com.web.pojo.Book;
import com.web.pojo.Category;
import com.web.pojo.User;
import com.web.repository.BookRepository;
import com.web.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author luwb
 * @date 2020/02/25
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BookServiceImpl extends BaseServiceImpl<Book, Integer> implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public JpaBaseRepository<Book, Integer> getRepository() {
        return this.bookRepository;
    }

    @Override
    public List<Book> findAllByState(int state) {
        if (state == 0) {
            return bookRepository.findAll();
        }
        return bookRepository.findAllByState(state);
    }

    @Override
    public Page<Book> findPageByBookNameLikeOrAuthorLike(int page, int size, Book book) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return bookRepository.findAll(createSpecification(book), pageable);
    }

    @Override
    public List<Book> findAllByIds(Integer[] ids) {
        return bookRepository.findAllByIdIn(ids);
    }

    @Override
    public Book update(int id, int state) {
        Optional<Book> optional = bookRepository.findById(id);
        if (optional.isPresent()) {
            Book book = optional.get();
            book.setState(state);
            return bookRepository.save(book);
        }
        return null;
    }

    @Override
    public List<Book> findAllByCategoryid(Integer categoryid) {
        if (categoryid == -1) {
            // 查出所有上架的，即状态为2
            return bookRepository.findAllByState(2);
        }
        return bookRepository.findAllByCategoryAndState(new Category(categoryid), 2);
    }

    @Override
    public BigDecimal totalPriceByBookids(Integer[] bookids) {
        return bookRepository.totalPriceByBookids(bookids);
    }

    @Override
    public boolean updateState(Integer[] bookids, int state) {
        try {
            for (Integer bookid : bookids) {
                Book book = bookRepository.findById(bookid).orElse(null);
                assert book != null;
                book.setState(state);
                bookRepository.save(book);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public List<Book> findAllByUserid(Integer userid) {
        return bookRepository.findAllByUser(new User(userid));
    }

    @Override
    public List<Book> searchByKey(String key) {
        String newKey = "%" + key + "%";
        return bookRepository.findAllByBookNameLikeOrAuthorLikeOrPublisherIsLike(newKey, newKey, newKey);
    }


    private Specification<Book> createSpecification(Book book) {
        return (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (book.getBookName() != null) {
                predicateList.add(cb.like(root.get("bookName"), "%" + book.getBookName() + "%"));
            }
            if (book.getAuthor() != null) {
                predicateList.add(cb.like(root.get("author"), "%" + book.getAuthor() + "%"));
            }
            if (book.getPublisher() != null) {
                predicateList.add(cb.like(root.get("publisher"), "%" + book.getPublisher() + "%"));
            }
            if (book.getCategory() != null && book.getCategory().getId() != null) {
                predicateList.add(cb.equal(root.get("category").get("id"), book.getCategory().getId()));
            }
            if (book.getState() != null) {
                predicateList.add(cb.equal(root.get("state"), book.getState()));
            } else {
                // 状态为-1的图书表示删除
                predicateList.add(cb.notEqual(root.get("state"), "-1"));
            }
            return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
        };
    }

}
