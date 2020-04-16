package com.web.service.impl;

import com.base.BaseServiceImpl;
import com.base.JpaBaseRepository;
import com.web.pojo.OrderDetails;
import com.web.repository.OrderDetailsRepository;
import com.web.service.OrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author luwb
 * @date 2020-02-29
 */
@Service
public class OrderDetailsServiceImpl extends BaseServiceImpl<OrderDetails,Integer> implements OrderDetailsService {

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Override
    public JpaBaseRepository<OrderDetails, Integer> getRepository() {
        return this.orderDetailsRepository;
    }

    @Override
    public List<OrderDetails> findAllByOrderId(int orderId) {
        return orderDetailsRepository.findAllByOrderId(orderId);
    }

    @Override
    public OrderDetails findOneByOrderIdAndBookId(int orderId, int bookId) {
        return orderDetailsRepository.findFirstByOrderIdAndBookId(orderId, bookId);
    }

}
