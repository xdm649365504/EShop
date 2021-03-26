package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoriesByPid(@RequestParam(value="pid",defaultValue = "0")Long pid){
         if(pid==null||pid<0){
             return ResponseEntity.badRequest().build();
         }
        List<Category> categories=categoryService.queryCategoriesByPid(pid);
         if(CollectionUtils.isEmpty(categories)){
             return ResponseEntity.notFound().build();
         }
         return ResponseEntity.ok(categories);

    }

    @GetMapping
    public ResponseEntity<List<String>> queryNamesByIds(@RequestParam("ids")List<Long> ids){
        List<String> categoryNames = this.categoryService.queryNamesByIds(ids);
        if(CollectionUtils.isEmpty(categoryNames)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categoryNames);
    }

    /**
     * 根据3级分类id，查询1~3级的分类
     * @param id
     * @return
     */
    @GetMapping("all/level")
    public ResponseEntity<List<Category>> queryAllByCid3(@RequestParam("id") Long id){
        List<Category> list = this.categoryService.queryAllByCid3(id);
        if (CollectionUtils.isEmpty(list)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);
    }
}
