package com.web.service;

import com.base.BaseService;
import com.web.pojo.Address;
import java.util.List;

public interface AddressService extends BaseService<Address,Integer> {

    List<Address> findAllByUserid(Integer userId);

    Address findDefaultAddress(Integer userId);

    boolean updateState(Integer userId, int addressId, int state);
}
