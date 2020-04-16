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
            Category category = book.getCategory();
            if (state == 2) {
                // 审核通过时，所在分类图书数量 +1
                category.setTotalCount(category.getTotalCount() + 1);
                categoryService.save(category);
            }
            if (state == 5) {
                // 原在出售的图书，下架（审核不通过）时，所在分类图书数量-1
                if (book.getState()==2) {
                    category.setTotalCount(category.getTotalCount() - 1);
                    categoryService.save(category);
                }
            }
            book.setState(state);
            return bookRepository.save(book);
        }
        return null;
    }

    @Override
    public List<Book> findAllByCategoryId(Integer categoryId) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        if (categoryId == -1) {
            // 查出所有上架的，即状态为2
            return bookRepository.findAllByState(2,sort);
        }
        return bookRepository.findAllByCategoryAndState(new Category(categoryId), 2,sort);
    }

    @Override
    public BigDecimal totalPriceByBookIds(Integer[] bookIds) {
        return bookRepository.totalPriceByBookIds(bookIds);
    }

    @Override
    public boolean updateState(Integer[] bookIds, int state) {
        try {
            for (Integer bookId : bookIds) {
                Book book = bookRepository.findById(bookId).orElse(null);
                assert book != null;
                book.setState(state);
                if (state == 3) {
                    // 图书卖出后所在分类图书数量-1
                    Category category = book.getCategory();
                    category.setTotalCount(category.getTotalCount() - 1);
                    categoryService.save(category);
                }
                bookRepository.save(book);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public List<Book> findAllByUserId(Integer userId) {
        return bookRepository.findAllByUser(new User(userId));
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
