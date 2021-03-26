package com.leyou.item.service;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> queryCategoriesByPid(Long pid) {
        Category record = new Category();
           record.setParentId(pid);
       return this.categoryMapper.select(record);
    }

    public List<String> queryNamesByIds(List<Long> ids){
        List<Category> categories = this.categoryMapper.selectByIdList(ids);
        List<String> categoryNameList = categories.stream().map(category -> {
                    String categoryName = category.getName();
                    return categoryName;
                }
        ).collect(Collectors.toList());
        return  categoryNameList;

    }

    public List<Category> queryAllByCid3(Long id) {
        Category cid3 = this.categoryMapper.selectByPrimaryKey(id);
        Category cid2 = this.categoryMapper.selectByPrimaryKey(cid3.getParentId());
        Category cid1 = this.categoryMapper.selectByPrimaryKey(cid2.getParentId());
        return Arrays.asList(cid1, cid2, cid3);
    }
}
