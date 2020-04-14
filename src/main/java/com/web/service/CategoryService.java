package com.web.service;

import com.base.BaseService;
import com.web.pojo.Category;

import java.util.List;

/**
 * @author luwb
 * @date 2020-02-29
 */
public interface CategoryService extends BaseService<Category,Integer>{

    List<Category> findAllByKey(String key);

}
