package com.leyou;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.search.client.BrandClient;
import com.leyou.search.search.client.GoodsClient;
import com.leyou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class ImportData {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private SearchService searchService;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private BrandClient brandClient;
    @RequestMapping("/importdata")
    public void importData(@RequestParam(value = "id",required = false,defaultValue = "1")Long id){
        this.elasticsearchTemplate.createIndex(Goods.class);
        this.elasticsearchTemplate.putMapping(Goods.class);
        Integer page = 1;
        Integer rows = 100;
        //自定义方法 可以不用写实现 springData会帮你实现 但是你的方法名需满足一定的规则
        List<Goods> list = goodsRepository.findByAll("华为");
        list.forEach(System.out::println);
        do {
            PageResult<SpuBo> result = this.goodsClient.querySpuBoBypage(null, null, page, rows);
            List<SpuBo> spuBos = result.getItems();
            List<Goods> goodsList = spuBos.stream().map(spuBo -> {
                try {
                    return this.searchService.buildGoods(spuBo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());
            //执行新增数据的方法
            this.goodsRepository.saveAll(goodsList);
            rows=spuBos.size();
            page++;
        }
        while(rows==100);
    }

    @RequestMapping("/ftest")
    public void test(){
        List<Sku> skus = this.goodsClient.querySkusBySpuId(1L);
        System.err.println(skus);
    }
}
