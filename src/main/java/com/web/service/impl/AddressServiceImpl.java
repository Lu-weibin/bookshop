package com.web.service.impl;

import com.base.BaseServiceImpl;
import com.base.JpaBaseRepository;
import com.web.pojo.Address;
import com.web.pojo.User;
import com.web.repository.AddressRepository;
import com.web.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author luwb
 * @date 2020-02-29
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AddressServiceImpl extends BaseServiceImpl<Address,Integer> implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public JpaBaseRepository<Address, Integer> getRepository() {
        return this.addressRepository;
    }

    @Override
    public List<Address> findAllByUserid(Integer userid) {
        return addressRepository.findAllByUserAndStateNot(new User(userid),-1);
    }

    @Override
    public Address findDefaultAddress(Integer userid) {
        return addressRepository.findFirstByUserAndState(new User(userid),2);
    }

    @Override
    public boolean updateState(Integer userid, int addressid, int state) {
        try {
            if (state == 2) {
                // 要更改默认地址时，要先将之前为默认的设置为非默认
                addressRepository.updateState(userid);
            }
            Optional<Address> optional = this.findById(addressid);
            if (optional.isPresent()) {
                Address address = optional.get();
                address.setState(state);
                this.save(address);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
