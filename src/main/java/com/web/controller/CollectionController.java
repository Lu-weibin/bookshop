package com.web.controller;

import com.base.Result;
import com.base.StatusCode;
import com.web.pojo.Collection;
import com.web.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author luwb
 * @date 2020-02-29
 */
@RestController
@RequestMapping("collection")
@CrossOrigin
public class CollectionController {

    @Autowired
    private CollectionService collectionService;
    @Autowired
    private HttpServletRequest request;

    @GetMapping
    public Result list() {
        Integer userid = (Integer) request.getSession().getAttribute("userid");
        return new Result(collectionService.findAllByUserid(userid));
    }

    @PostMapping("book/{bookid}")
    public Result addCollection(@PathVariable int bookid) {
        Integer userid = (Integer) request.getSession().getAttribute("userid");
        if (userid == null) {
            return new Result(false, StatusCode.LOGINERROR, "未登录!");
        }
        // 校验该用户是否已收藏过该书籍
        Collection existCollection = collectionService.findByUseridAndBookid(userid, bookid);
        if (existCollection != null) {
            return new Result(false, StatusCode.ERROR, "已收藏过，不要重复收藏!");
        }
        Collection collection = new Collection();
        collection.setUserid(userid);
        collection.setBookid(bookid);
        return new Result(collectionService.save(collection));
    }

    /**
     * 删除收藏
     */
    @GetMapping("delete/{bookid}")
    public Result delete(@PathVariable int bookid) {
        Integer userid = (Integer) request.getSession().getAttribute("userid");
        if (userid == null) {
            return new Result(false, StatusCode.LOGINERROR, "未登录!");
        }
        if (collectionService.deleteCollection(userid, bookid)) {
            return new Result("删除成功!");
        }
        return new Result(false, StatusCode.ERROR, "删除失败!");
    }

}
