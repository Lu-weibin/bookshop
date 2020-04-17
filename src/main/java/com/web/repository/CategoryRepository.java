package com.web.repository;

import com.base.JpaBaseRepository;
import com.web.pojo.Category;
import java.util.List;

public interface CategoryRepository extends JpaBaseRepository<Category, Integer> {

    List<Category> findAllByCategoryLike(String key);

}
