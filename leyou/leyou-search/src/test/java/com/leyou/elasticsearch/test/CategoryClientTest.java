package com.leyou.elasticsearch.test;

import com.leyou.LeyouSearchApplication;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.search.search.client.BrandClient;
import com.leyou.search.search.client.CategoryClient;
import com.leyou.search.search.client.GoodsClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaAutoServiceRegistration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LeyouSearchApplication.class)
public class CategoryClientTest {

    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
//    @MockBean
//    private EurekaAutoServiceRegistration eurekaAutoServiceRegistration;
    @Test
    public void testQueryCategories() {
//        List<String> names = this.categoryClient.queryNamesByIds(Arrays.asList(1L, 2L, 3L));
////        names.forEach(System.out::println);
      //this.goodsClient.querySpuBoBypage(null,null,1,5);
        this.goodsClient.querySkusBySpuId(1L);

    }
    @Test
    public void delete(){
        elasticsearchTemplate.deleteIndex("goods");
    }

    @Test
    public  void feignTest(){
        Brand brand = this.brandClient.queryBrandById(1l);
        System.out.println(brand.getName());
//        this.goodsClient.querySpuBoBypage(null,null,1,5);
    }
}