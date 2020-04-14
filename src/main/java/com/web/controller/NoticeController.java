package com.web.controller;

import com.base.Result;
import com.base.StatusCode;
import com.web.pojo.Notice;
import com.web.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("titles")
    public Result getTitles() {
        List<Notice> notices = noticeService.findAllByState(1);
        for (Notice notice : notices) {
            notice.setContent("");
        }
        return new Result(notices);
    }

    @GetMapping("firstNotice")
    public Result getFirstNotice() {
        return new Result(noticeService.getFirstNotice());
    }

    @GetMapping("{noticeid}")
    public Result getNoticeById(@PathVariable int noticeid) {
        Optional<Notice> optionalNotice = noticeService.findById(noticeid);
        return optionalNotice.map(Result::new).orElseGet(() -> new Result(false, StatusCode.ERROR, "查询不到数据!"));
    }

    @GetMapping("list")
    public Result list() {
        return new Result(noticeService.findAll("time", Sort.Direction.DESC));
    }

}
