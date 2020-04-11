package com.web.repository;

import com.base.JpaBaseRepository;
import com.web.pojo.Orders;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * @author luwb
 * @date 2020-02-29
 */
public interface OrderRepository extends JpaBaseRepository<Orders,Integer> {

    List<Orders> findAllByUserid(int userid, Sort sort);

    List<Orders> findAllByUseridAndState(int userid, int state, Sort sort);
}
