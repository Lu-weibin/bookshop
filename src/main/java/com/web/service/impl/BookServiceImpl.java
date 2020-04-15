package com.web.service.impl;

import com.base.BaseServiceImpl;
import com.base.JpaBaseRepository;
import com.web.pojo.Book;
import com.web.pojo.Category;
import com.web.pojo.User;
import com.web.repository.BookRepository;
import com.web.service.BookService;
import com.web.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private final CategoryService categoryService;

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.categoryService = categoryService;
    }

    @Override
    public JpaBaseRepository<Book, Integer> getRepository() {
        return this.bookRepository;
    }

    @Override
    public List<Book> findAllByState(int state) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        if (state == 0) {
            return bookRepository.findAll(sort);
        }
        return bookRepository.findAllByState(state, sort);
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
            if (state == 2) {
                // 审核通过时，所在分类图书数量 +1
                Category category = book.getCategory();
                category.setTotalCount(category.getTotalCount() + 1);
                categoryService.save(category);
            }
            return bookRepository.save(book);
        }
        return null;
    }

    @Override
    public List<Book> findAllByCategoryid(Integer categoryid) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        if (categoryid == -1) {
            // 查出所有上架的，即状态为2
            return bookRepository.findAllByState(2,sort);
        }
        return bookRepository.findAllByCategoryAndState(new Category(categoryid), 2,sort);
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

    @Override
    public List<Book> search(String bookName, String author, String publisher, Integer categoryId, Integer state) {
        Specification<Book> specification = createSpecification(bookName, author, publisher, categoryId, state);
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        return bookRepository.findAll(specification, sort);
    }


    private Specification<Book> createSpecification(String bookName, String author, String publisher, Integer categoryId, Integer state) {
        return (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (bookName != null && !"".equals(bookName)) {
                predicateList.add(cb.like(root.get("bookName"), "%" + bookName + "%"));
            }
            if (author != null && !"".equals(author)) {
                predicateList.add(cb.like(root.get("author"), "%" + author + "%"));
            }
            if (publisher != null && !"".equals(publisher)) {
                predicateList.add(cb.like(root.get("publisher"), "%" + publisher + "%"));
            }
            if ( categoryId != null) {
                predicateList.add(cb.equal(root.get("category").get("id"), categoryId));
            }
            if (state != null) {
                predicateList.add(cb.equal(root.get("state"), state));
            }
            return cb.and(predicateList.toArray(new Predicate[0]));
        };
    }

}
