package com.web.repository;

import com.base.JpaBaseRepository;
import com.web.pojo.Orders;
import org.springframework.data.domain.Sort;
import java.util.List;

public interface OrderRepository extends JpaBaseRepository<Orders,Integer> {

    List<Orders> findAllByUserId(int userId, Sort sort);

    List<Orders> findAllByUserIdAndState(int userId, int state, Sort sort);
}
