package com.web.controller;

import com.base.Result;
import com.base.StatusCode;
import com.web.pojo.Notice;
import com.web.service.NoticeService;
import com.web.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @GetMapping("search")
    public Result search(@RequestParam String key,@RequestParam Integer state) {
        List<Notice> notices = noticeService.search(key);
        notices = state == null ? notices : notices.stream().filter((notice -> notice.getState().equals(state))).collect(Collectors.toList());
        return new Result(notices);
    }

    @PostMapping("saveNotice")
    public Result saveNotice(Notice notice){
        notice.setTime(CommonUtil.now());
        Integer state = notice.getState();
        // 如果是从已发布的公告编辑后存入草稿，则是新建一条草稿记录，而不是将原来的发布状态改为草稿
        if (state == 2 && notice.getId() != null) {
            Notice notice1 = noticeService.findById(notice.getId()).orElse(new Notice());
            if (notice1.getState() == 1) {
                notice.setId(null);
            }
        }
        return new Result(noticeService.save(notice));
    }

    /**
     * 读取最近一条通知草稿
     */
    @GetMapping("readNotice")
    public Result readNotice(){
        List<Notice> notices = noticeService.findAllByState(2);
        if (notices.isEmpty()) {
            return new Result(false, StatusCode.ERROR, "草稿箱暂无内容");
        }
        return new Result(notices.get(0));
    }

    @PostMapping("delete/{noticeId}")
    public Result delete(@PathVariable Integer noticeId) {
        noticeService.delete(noticeId);
        return new Result("删除成功！");
    }

}
