package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.sql.ResultSet;
import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;


    /**
     * 根根据查询条件分页查询品牌信息
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    public PageResult<Brand> queryBrandsBypage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        //根据name模糊查询
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("name","%"+key+"%").orEqualTo("letter",key);
        }


         //添加分页条件
        PageHelper.startPage(page,rows);
         //添加排序条件
        if(StringUtils.isNotBlank(sortBy)){
            example.setOrderByClause(sortBy + " "+(desc ? "desc":"asc"));
        }
        List<Brand> brands = brandMapper.selectByExample(example);

        PageInfo<Brand> pageInfo = new PageInfo<>(brands);

       return  new PageResult<>(pageInfo.getTotal(),pageInfo.getList());

    }

    /**
     * 新增品牌
     * @param brand
     * @param cids
     */
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        //先新增brand
       Boolean flag= brandMapper.insertSelective(brand)==1;
        //再新增中间表
            cids.forEach(cid->{
                  brandMapper.insertCategoryAndBrand(cid,brand.getId());
                    }
                    );

    }

    public List<Brand> queryBrandsByCid(Long cid) {
        return this.brandMapper.selectBrandsByCid(cid);
    }

    public Brand querBrandById(Long id) {
        return this.brandMapper.selectByPrimaryKey(id);
    }
}
