package com.web.controller;

import com.base.Result;
import com.base.StatusCode;
import com.web.pojo.Address;
import com.web.pojo.User;
import com.web.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

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

    @PostMapping("add/{address}")
    public Result add(@PathVariable String address) {
        Integer userid = (Integer) request.getSession().getAttribute("userid");
        Address address1 = new Address();
        address1.setAddress(address);
        address1.setUser(new User(userid));
        address1.setState(1);
        return new Result(addressService.save(address1));
    }

    @GetMapping("defaultAddress")
    public Result getDefaultAddress() {
        Integer userid = (Integer) request.getSession().getAttribute("userid");
        Address defaultAddress = addressService.findDefaultAddress(userid);
        return new Result(defaultAddress == null ? "" : defaultAddress.getAddress());
    }

    @PostMapping("state/{addressid}/{state}")
    public Result updateState(@PathVariable int addressid, @PathVariable int state) {
        Integer userid = (Integer) request.getSession().getAttribute("userid");
        boolean result = addressService.updateState(userid, addressid, state);
        if (result) {
            return new Result("操作成功");
        }
        return new Result(false, StatusCode.ERROR,"操作失败");
    }

}
