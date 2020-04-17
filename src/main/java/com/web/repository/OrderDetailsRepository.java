package com.web.repository;

import com.base.JpaBaseRepository;
import com.web.pojo.OrderDetails;

import java.util.List;

public interface OrderDetailsRepository extends JpaBaseRepository<OrderDetails,Integer>{

    List<OrderDetails> findAllByOrderId(int orderId);

    OrderDetails findFirstByOrderIdAndBookId(int orderId, int bookId);

}
