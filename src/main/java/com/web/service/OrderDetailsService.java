package com.web.service;

import com.base.BaseService;
import com.web.pojo.OrderDetails;
import java.util.List;

public interface OrderDetailsService extends BaseService<OrderDetails,Integer> {

    List<OrderDetails> findAllByOrderId(int orderId);

    OrderDetails findOneByOrderIdAndBookId(int orderId, int bookId);
}
