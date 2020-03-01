package com.web.service.impl;

import com.base.BaseServiceImpl;
import com.base.JpaBaseRepository;
import com.web.pojo.Address;
import com.web.repository.AddressRepository;
import com.web.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author luwb
 * @date 2020-02-29
 */
@Service
public class AddressServiceImpl extends BaseServiceImpl<Address,Integer> implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public JpaBaseRepository<Address, Integer> getRepository() {
        return this.addressRepository;
    }

}
