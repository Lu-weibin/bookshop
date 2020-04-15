package com.web.controller;

import com.base.Result;
import com.base.StatusCode;
import com.web.pojo.Book;
import com.web.pojo.Category;
import com.web.pojo.User;
import com.web.service.BookService;
import com.web.service.UserService;
import com.web.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("book")
public class BookController {

	private final BookService bookService;

	private final UserService userService;

	private final HttpServletRequest request;

	@Value("${imagePath}")
	private String imagePath;

	@Autowired
	public BookController(BookService bookService, HttpServletRequest request, UserService userService) {
		this.bookService = bookService;
		this.request = request;
		this.userService = userService;
	}

	@GetMapping("category/{categoryId}")
	public Result listByCategoryId(@PathVariable Integer categoryId) {
		// categoryd为-1时返回所有书籍
		return new Result(bookService.findAllByCategoryid(categoryId));
	}

	@GetMapping("{id}")
	public Result listById(@PathVariable int id) {
		return new Result(bookService.findById(id).orElse(null));
	}

	@GetMapping("state/{state}")
	public Result listByState(@PathVariable int state) {
		return new Result(bookService.findAllByState(state));
	}

	@GetMapping("search")
	public Result search(@RequestParam String bookName, @RequestParam String author, @RequestParam String publisher, @RequestParam Integer categoryId, @RequestParam Integer state) {
		List<Book> books = bookService.search(bookName, author, publisher, categoryId, state);
		return new Result(books);
	}

	@GetMapping("search/{key}")
	public Result searchByKey(@PathVariable String key) {
		if (CommonUtil.isNullOrEmpty(key)) {
			return new Result(Collections.emptyList());
		}
		List<Book> books = bookService.searchByKey(key);
		books.removeIf(book -> book.getState() != 2);
		return new Result(books);
	}

	@PostMapping("{id}/{state}")
	public Result updateState(@PathVariable int id,@PathVariable int state){
		bookService.update(id, state);
		return new Result("操作成功");
	}

	@PostMapping("add")
	public Result addBooks(MultipartFile file, String bookName, String author, String publisher, String publishTime, String price, String category, String conditions, String description) {
		Integer userid = (Integer) request.getSession().getAttribute("userid");
		if (userid == null) {
			return new Result(false, StatusCode.ERROR, "未登录！");
		}
		Book book = new Book();
		book.setUser(new User(userid));
		book.setBookName(bookName);
		book.setAuthor(author);
		book.setPublisher(publisher);
		try {
			Timestamp publishDate = new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(publishTime).getTime());
			if (CommonUtil.getTimeDifference(CommonUtil.now(),publishDate)<0) {
				return new Result(false, StatusCode.ERROR, "出版时间有误！");
			}
			book.setPublishTime(publishDate);
			book.setPrice(new BigDecimal(price));
			book.setCategory(new Category(Integer.parseInt(category)));
		} catch (ParseException e) {
			return new Result(false, StatusCode.ERROR, "日期格式有误！");
		} catch (Exception e) {
			return new Result(false, StatusCode.ERROR, "输入信息有误，请检查！");
		}
		book.setConditions(conditions);
		book.setDescription(description);
		// 待审核状态
		book.setState(1);
		book.setCreateTime(CommonUtil.now());
		try {
			String savePath = handleImage(file, userid);
			book.setPicture(savePath);
		} catch (IOException e) {
			return new Result(false, StatusCode.ERROR, "图片上传失败！");
		}
		bookService.save(book);
		return new Result(true, StatusCode.OK, "发布成功，等待审核！");
	}

	@GetMapping("findAllByUserid")
	public Result findAllByUserd() {
		Integer userid = (Integer) request.getSession().getAttribute("userid");
		List<Book> books = bookService.findAllByUserid(userid);
		for (Book book : books) {
			// 该图书为卖出或退货状态时，查找购买者I
			if (book.getState() == 3 || book.getState() == 4) {
				Integer bookId = book.getId();
				User user = userService.findOneByBookId(bookId);
				if (user == null) {
					user = new User();
				}
				// 此处的user为购买该图书的用户
				book.setUser(user);
			}
		}
		return new Result(books);
	}

	@GetMapping("list")
	public Result list() {
		return new Result(bookService.findAll("createTime", Sort.Direction.DESC));
	}

	private String handleImage(MultipartFile imgFile,int userid) throws IOException {
		if (imgFile == null) {
			return "";
		}
		// 保存到当前项目img目录下，路径：img/用户id/原图片名+_日期
		String newFileName = Objects.requireNonNull(imgFile.getOriginalFilename()).replace(".", "_"+CommonUtil.formatDate(new Date(), "yyyyMMdd") + ".");
		String descStr = String.format("%s\\%d\\%s", imagePath, userid, newFileName);
		File desc = new File(descStr);
		if (!desc.getParentFile().exists()) {
			desc.getParentFile().mkdirs();
		}
		System.out.println(desc);
		imgFile.transferTo(desc);
		return String.format("img/book/%d/%s", userid, newFileName);
	}

}
