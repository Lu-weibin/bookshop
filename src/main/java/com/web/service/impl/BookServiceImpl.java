package com.web.service.impl;

import com.base.BaseServiceImpl;
import com.base.JpaBaseRepository;
import com.web.pojo.Book;
import com.web.repository.BookRepository;
import com.web.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public List<Book> findAllByBookNameLikeOrAuthorLike(String key) {
		return bookRepository.findByBookNameContainsOrAuthorContains(key,key);
	}
}
