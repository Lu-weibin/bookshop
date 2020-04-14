package com.web.service.impl;

import com.base.BaseServiceImpl;
import com.base.JpaBaseRepository;
import com.web.pojo.Category;
import com.web.repository.CategoryRepository;
import com.web.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author luwb
 * @date 2020-02-29
 */
@Service
public class CategoryServiceImpl extends BaseServiceImpl<Category,Integer> implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public JpaBaseRepository<Category, Integer> getRepository() {
        return this.categoryRepository;
    }

    @Override
    public List<Category> findAllByKey(String key) {
        return categoryRepository.findAllByCategoryLike(key);
    }
}
