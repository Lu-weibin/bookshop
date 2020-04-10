package com.web.service;

import com.base.BaseService;
import com.web.pojo.Address;

import java.util.List;

/**
 * @author luwb
 * @date 2020-02-29
 */
public interface AddressService extends BaseService<Address,Integer> {

    List<Address> findAllByUserid(Integer userid);

    Address findDefaultAddress(Integer userid);

    boolean updateState(Integer userid, int addressid, int state);
}
