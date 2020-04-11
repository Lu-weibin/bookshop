package com.web.repository;

import com.base.JpaBaseRepository;
import com.web.pojo.Notice;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface NoticeRepository extends JpaBaseRepository<Notice,Integer> {

    Notice findFirstByState(int state, Sort sort);

    List<Notice> findAllByState(int state);


}
