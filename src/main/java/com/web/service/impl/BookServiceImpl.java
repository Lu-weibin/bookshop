package com.web.service.impl;

import com.base.BaseServiceImpl;
import com.base.JpaBaseRepository;
import com.web.pojo.Book;
import com.web.repository.BookRepository;
import com.web.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author luwb
 * @date 2020/02/25
 */
@Service
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
	public Book update(int id, int state) {
		Optional<Book> optional = bookRepository.findById(id);
		if (optional.isPresent()) {
			Book book = optional.get();
			book.setState(state);
			return bookRepository.save(book);
		}
		return null;
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
