package com.web.service;

import com.base.BaseService;
import com.web.pojo.Orders;
import java.util.List;

public interface OrderService extends BaseService<Orders,Integer> {

    List<Orders> findAllByUserId(int parseInt,int state);
}
