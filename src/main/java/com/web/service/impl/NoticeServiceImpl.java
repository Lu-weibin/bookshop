package com.web.service.impl;

import com.base.BaseServiceImpl;
import com.base.JpaBaseRepository;
import com.web.pojo.Notice;
import com.web.repository.NoticeRepository;
import com.web.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class NoticeServiceImpl extends BaseServiceImpl<Notice, Integer> implements NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;


    @Override
    public JpaBaseRepository<Notice, Integer> getRepository() {
        return this.noticeRepository;
    }

    @Override
    public Notice getFirstNotice() {
        Sort sort = new Sort(Sort.Direction.DESC, "time");
        return noticeRepository.findFirstByState(1, sort);
    }

    @Override
    public List<Notice> findAllByState(int state) {
        return noticeRepository.findAllByState(state);
    }

}
