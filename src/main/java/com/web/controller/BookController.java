package com.web.controller;

import com.base.PageResult;
import com.base.Result;
import com.base.StatusCode;
import com.web.pojo.Book;
import com.web.pojo.User;
import com.web.service.BookService;
import com.web.util.CommonUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

/**
 * @author luwb
 * @date 2020/02/25
 */
@CrossOrigin
@RestController
@RequestMapping("book")
public class BookController {

	private final BookService bookService;

	private final HttpServletRequest request;

	@Autowired
	public BookController(BookService bookService, HttpServletRequest request) {
		this.bookService = bookService;
		this.request = request;
	}

	@GetMapping("category/{categoryid}")
	public Result listByCategoryid(@PathVariable Integer categoryid) {
		return new Result(bookService.findAllByCategoryid(categoryid));
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
		Page<Book> bookPage = bookService.findPageByBookNameLikeOrAuthorLike(page, size, book);
		return new Result(new PageResult<>(bookPage.getTotalElements(),bookPage.getContent()));
	}

	@PostMapping
	public Result save(@RequestBody Book book){
		Claims userClaims = (Claims) request.getAttribute("user_claims");
		if (userClaims != null) {
			book.setCreateTime(CommonUtil.now());
			// 网站用户添加的图书需要管理员审核
			book.setState(1);
			book.setUser(new User(Integer.parseInt(userClaims.getId())));
			bookService.save(book);
			return new Result("新增成功");
		}
		Claims adminClaims = (Claims) request.getAttribute("admin_claims");
		if (adminClaims != null) {
			book.setCreateTime(CommonUtil.now());
			// 管理员添加的图书状态不用核审，即已上架的状态
			book.setState(2);
			book.setUser(new User(Integer.parseInt(adminClaims.getId())));
			bookService.save(book);
			return new Result("新增成功");
		}
		return new Result(false, StatusCode.ACCESSERROR, "限权不足");
	}

	@PutMapping("{id}")
	public Result update(@RequestBody Book book){
		bookService.save(book);
		return new Result("更新成功");
	}

	@PutMapping("{id}/{state}")
	public Result updateState(@PathVariable int id,@PathVariable int state){
		bookService.update(id, state);
		return new Result("操作成功");
	}

	@DeleteMapping("{id}")
	public Result delete(@PathVariable int id){
		// 逻辑删除，把图书状态改为-1
		bookService.update(id, -1);
		return new Result("删除成功");
	}

}
