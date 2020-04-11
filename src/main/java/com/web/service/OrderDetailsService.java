package com.web.service;

import com.base.BaseService;
import com.web.pojo.OrderDetails;

import java.util.List;

/**
 * @author luwb
 * @date 2020-02-29
 */
public interface OrderDetailsService extends BaseService<OrderDetails,Integer> {

    List<OrderDetails> findAllByOrderid(int orderid);

    OrderDetails findOneByOrderidAndBookid(int orderid, int bookid);
}
