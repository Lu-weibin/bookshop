package com.web.controller;

import com.base.Result;
import com.base.StatusCode;
import com.web.pojo.Address;
import com.web.pojo.User;
import com.web.service.AddressService;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("address")
@CrossOrigin
public class AddressController {

    private final AddressService addressService;
    private HttpServletRequest request;

    public AddressController(AddressService addressService, HttpServletRequest request) {
        this.addressService = addressService;
        this.request = request;
    }

    @GetMapping
    public Result list() {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        return new Result(addressService.findAllByUserid(userId));
    }

    @PostMapping("add/{address}")
    public Result add(@PathVariable String address) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        Address address1 = new Address();
        address1.setAddress(address);
        address1.setUser(new User(userId));
        address1.setState(1);
        return new Result(addressService.save(address1));
    }

    @GetMapping("defaultAddress")
    public Result getDefaultAddress() {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        Address defaultAddress = addressService.findDefaultAddress(userId);
        return new Result(defaultAddress == null ? "" : defaultAddress.getAddress());
    }

    @PostMapping("state/{addressId}/{state}")
    public Result updateState(@PathVariable int addressId, @PathVariable int state) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        boolean result = addressService.updateState(userId, addressId, state);
        if (result) {
            return new Result("操作成功");
        }
        return new Result(false, StatusCode.ERROR,"操作失败");
    }

}
