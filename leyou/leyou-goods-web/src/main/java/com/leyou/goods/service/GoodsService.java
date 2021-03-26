package com.leyou.goods.service;

import com.leyou.goods.client.BrandClient;
import com.leyou.goods.client.CategoryClient;
import com.leyou.goods.client.GoodsClient;
import com.leyou.goods.client.SpecificationClient;
import com.leyou.item.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class GoodsService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    public Map<String,Object> loadData(Long spuId){
        Map<String,Object> model=new HashMap<>();

        //根据spuId查询spu
        Spu spu = this.goodsClient.querySpuById(spuId);

        //查询spudetail
        SpuDetail spuDetail = this.goodsClient.querySpuDetailById(spuId);
        //查询分类名称信息 map<String,Object>
        List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        List<String> names = this.categoryClient.queryNamesByIds(cids);
        List<Map<String,Object>> categories=new ArrayList<>();
        for (int i = 0; i < cids.size(); i++) {
            Map<String,Object> map=new HashMap<>();
            map.put("id",cids.get(i));
            map.put("name",names.get(i));
            categories.add(map);
        }
        //查询品牌
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());
        //查询skus
        List<Sku> skus = this.goodsClient.querySkusBySpuId(spuId);
        //查询规格参数组
        List<SpecGroup> groups = this.specificationClient.queryGroupsWithParam(spu.getCid3());
        //查询特殊规格参数
        List<SpecParam> params = this.specificationClient.queryParamsByCid(null, spu.getCid3(), false, null);
        Map<Long,String> paramMap=new HashMap<>();
        params.forEach(param->{
            paramMap.put(param.getId(),param.getName());
        });
      /*  //服务端可以通过HttpServletRequest获取到前面传递的参数，具体获取逻辑如下：前提设置
          //编写拦截器类实现 feign.RequestInterceptor并重写 apply()方法
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        request.getHeader("某个feign.RequestInterceptor中设置的头信息");*/

        model.put("spu",spu);
        model.put("spuDetail",spuDetail);
        model.put("categories",categories);
        model.put("brand",brand);
        model.put("skus",skus);
        model.put("groups",groups);
        model.put("paramMap",paramMap);
        return  model;
    }
}
