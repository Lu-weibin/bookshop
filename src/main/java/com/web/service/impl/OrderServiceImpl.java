package com.web.service.impl;

import com.base.BaseServiceImpl;
import com.base.JpaBaseRepository;
import com.web.pojo.Orders;
import com.web.repository.OrderRepository;
import com.web.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author luwb
 * @date 2020-02-29
 */
@Service
public class OrderServiceImpl extends BaseServiceImpl<Orders,Integer> implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public JpaBaseRepository<Orders, Integer> getRepository() {
        return this.orderRepository;
    }

    @Override
    public List<Orders> findAllByUserid(int userid,int state) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        if (state == -1) {
            return orderRepository.findAllByUserid(userid, sort);
        }
        return orderRepository.findAllByUseridAndState(userid, state, sort);
    }
}
