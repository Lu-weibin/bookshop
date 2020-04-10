package com.web.controller;

import com.base.Result;
import com.web.pojo.Address;
import com.web.pojo.User;
import com.web.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author luwb
 * @date 2020-02-29
 */
@RestController
@RequestMapping("address")
@CrossOrigin
public class AddressController {

    @Autowired
    private AddressService addressService;
    @Autowired
    private HttpServletRequest request;

    @GetMapping
    public Result list() {
        Integer userid = (Integer) request.getSession().getAttribute("userid");
        return new Result(addressService.findAllByUserid(userid));
    }

    @PostMapping("add")
    public Result add(String address) {
        Integer userid = (Integer) request.getSession().getAttribute("userid");
        Address address1 = new Address();
        address1.setAddress(address);
        address1.setUser(new User(userid));
        return new Result(addressService.save(address1));
    }

}
