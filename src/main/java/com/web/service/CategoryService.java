package com.web.service;

import com.base.BaseService;
import com.web.pojo.Category;

import java.util.List;

public interface CategoryService extends BaseService<Category,Integer>{

    List<Category> findAllByKey(String key);

}
