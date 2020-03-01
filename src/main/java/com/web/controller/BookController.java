package com.web.controller;

import com.base.Result;
import com.web.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luwb
 * @date 2020/02/25
 */
@RestController
@RequestMapping("book")
public class BookController {

	private final BookService bookService;

	@Autowired
	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@GetMapping
	public Result list() {
		return new Result(bookService.findAll());
	}

	@GetMapping("{id}")
	public Result listById(@PathVariable int id) {
		return new Result(bookService.findById(id).orElse(null));
	}

	@GetMapping("state/{state}")
	public Result listByState(@PathVariable int state) {
		return new Result(bookService.findAllByState(state));
	}

	@GetMapping("search/{key}")
	public Result listByBookNameOrAuthor(@PathVariable String key) {
		return new Result(bookService.findAllByBookNameLikeOrAuthorLike(key));
	}

}
