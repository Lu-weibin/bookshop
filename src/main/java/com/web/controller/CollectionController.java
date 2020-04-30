package com.web.controller;

import com.base.Result;
import com.web.pojo.Collection;
import com.web.service.CollectionService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("collection")
@CrossOrigin
public class CollectionController {

    private final CollectionService collectionService;
    private final HttpServletRequest request;

    public CollectionController(CollectionService collectionService, HttpServletRequest request) {
        this.collectionService = collectionService;
        this.request = request;
    }

    @GetMapping
    public Result list() {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        return new Result(collectionService.findAllByUserId(userId));
    }

    @PostMapping("book/{bookId}")
    public Result addCollection(@PathVariable int bookId) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if (userId == null) {
            return new Result(false, "未登录!");
        }
        // 校验该用户是否已收藏过该书籍
        Collection existCollection = collectionService.findByUserIdAndBookId(userId, bookId);
        if (existCollection != null) {
            return new Result(false, "已收藏过，不要重复收藏!");
        }
        Collection collection = new Collection();
        collection.setUserId(userId);
        collection.setBookId(bookId);
        return new Result(collectionService.save(collection));
    }

    /**
     * 删除收藏
     */
    @GetMapping("delete/{bookId}")
    public Result delete(@PathVariable int bookId) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if (userId == null) {
            return new Result(false, "未登录!");
        }
        if (collectionService.deleteCollection(userId, bookId)) {
            return new Result(true, "删除成功!");
        }
        return new Result(false, "删除失败!");
    }

}
