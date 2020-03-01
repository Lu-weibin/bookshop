package com.web.controller;

import com.base.Result;
import com.web.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luwb
 * @date 2020-02-29
 */
@RestController
@RequestMapping("collection")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @GetMapping
    public Result list() {
        return new Result(collectionService.findAll());
    }

}
