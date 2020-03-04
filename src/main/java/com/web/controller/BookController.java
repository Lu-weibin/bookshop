package com.web.controller;

import com.base.PageResult;
import com.base.Result;
import com.web.pojo.Book;
import com.web.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * @author luwb
 * @date 2020/02/25
 */
@CrossOrigin
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

	@PostMapping("search/{page}/{size}")
	public Result pageByBookNameOrAuthor(@PathVariable int page, @PathVariable int size, @RequestBody Book book) {
		System.out.println(book);
		Page<Book> bookPage = bookService.findPageByBookNameLikeOrAuthorLike(page, size, book);
		return new Result(new PageResult<>(bookPage.getTotalElements(),bookPage.getContent()));
	}

}
