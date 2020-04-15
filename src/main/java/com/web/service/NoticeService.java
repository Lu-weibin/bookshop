package com.web.service;

import com.base.BaseService;
import com.web.pojo.Notice;

import java.util.List;

public interface NoticeService extends BaseService<Notice,Integer> {

    Notice getFirstNotice();

    List<Notice> findAllByState(int state);

    List<Notice> search(String key);


}
