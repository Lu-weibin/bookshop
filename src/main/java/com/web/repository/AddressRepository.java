package com.web.repository;

import com.base.JpaBaseRepository;
import com.web.pojo.Address;
import com.web.pojo.User;

import java.util.List;

/**
 * @author luwb
 * @date 2020-02-29
 */
public interface AddressRepository extends JpaBaseRepository<Address,Integer>{

    List<Address> findAllByUser(User user);

}
