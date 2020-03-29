package com.web.service;

import com.base.BaseService;
import com.web.pojo.Orders;

import java.util.List;

/**
 * @author luwb
 * @date 2020-02-29
 */
public interface OrderService extends BaseService<Orders,Integer> {

    List<Orders> findAllByUserid(int parseInt,Integer state);
}
