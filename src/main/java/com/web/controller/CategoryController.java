package com.web.controller;

import com.base.Result;
import com.base.StatusCode;
import com.web.pojo.Category;
import com.web.service.BookService;
import com.web.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("category")
public class CategoryController {

    private final CategoryService categoryService;
    private final BookService bookService;

    public CategoryController(CategoryService categoryService, BookService bookService) {
        this.categoryService = categoryService;
        this.bookService = bookService;
    }

    @GetMapping("list")
    public Result list() {
        return new Result(categoryService.findAll());
    }

    @PostMapping("add/{category}")
    public Result add(@PathVariable String category) {
        if (!categoryService.findAllByKey(category).isEmpty()) {
            return new Result(false, StatusCode.ERROR, "已存在相同的分类名称！");
        }
        return new Result(categoryService.save(new Category(null, category, 0L)));
    }

    @PostMapping("delete/{categoryId}")
    public Result add(@PathVariable int categoryId) {
        try {
            categoryService.delete(categoryId);
        } catch (Exception e) {
            return new Result(false, StatusCode.ERROR, "删除失败，有其他非在售图书属于此类别");
        }
        return new Result("删除成功");
    }

    @GetMapping("search/{key}")
    public Result search(@PathVariable String key) {
        return new Result(categoryService.findAllByKey("%"+key+"%"));
    }


    /**
     * 生成类别对应的上架图书数量
     */
    @GetMapping("generateCount")
    public Result generateCount() {
        List<Category> categories = categoryService.findAll();
        for (Category category : categories) {
            Integer categoryId = category.getId();
            int count = bookService.findAllByCategoryId(categoryId).size();
            category.setTotalCount((long) count);
            categoryService.save(category);
        }
        return new Result("执行成功");
    }

    @PostMapping("update/{categoryId}/{category}")
    public Result update(@PathVariable int categoryId,@PathVariable String category) {
        List<Category> categories = categoryService.findAllByKey(category);
        if (!categories.isEmpty() && categories.get(0).getId() != categoryId) {
            return new Result(false, StatusCode.ERROR, "已存在相同的分类名称！");
        }
        Category updateCategory = categoryService.findById(categoryId).orElse(null);
        assert updateCategory != null;
        updateCategory.setCategory(category);
        return new Result(categoryService.save(updateCategory));
    }

}
