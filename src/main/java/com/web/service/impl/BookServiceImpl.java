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

/**
 * @author luwb
 * @date 2020/02/25
 */
@Service
public class BookServiceImpl extends BaseServiceImpl<Book,Integer> implements BookService {

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

	private Specification<Book> createSpecification(Book book) {
		return (root,query,cb) -> {
			List<Predicate> predicateList = new ArrayList<>();
			if (book.getBookName() != null) {
				predicateList.add(cb.like(root.get("bookName"), "%" + book.getBookName() + "%"));
			}
			if (book.getAuthor() != null) {
				predicateList.add(cb.like(root.get("author"), "%" + book.getAuthor() + "%"));
			}
			return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
		};
	}

}
