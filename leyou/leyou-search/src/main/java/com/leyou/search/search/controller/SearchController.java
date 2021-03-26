package com.leyou.search.search.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Spu;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.search.client.GoodsClient;
import com.leyou.search.service.SearchService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import javafx.beans.DefaultProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@DefaultProperties(defaultFallback = "testHystrixFallbackMethod")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @Autowired
    private GoodsClient goodsClient;
    @PostMapping("page")
    public ResponseEntity<SearchResult> search(@RequestBody SearchRequest searchRequest){
        SearchResult result= this.searchService.search(searchRequest);
        if(result==null|| CollectionUtils.isEmpty(result.getItems())){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
    @GetMapping("testHystrix")
    @ResponseBody //和fallback返回值相同返回字符串提醒 也可
    // 以取消这个 @ResponseBody但是下面fallback方法返回值也应该修改成一个路径字符串 不然报404找不到页面 或者不要用String做返回值
    @HystrixCommand // 标记该方法需要熔断
    public String test(){
        String forObject = new RestTemplate().getForObject("http://localhsot:8086", String.class);
          return forObject;
    }

    public String  testHystrixFallbackMethod(){
        return "服务器异常，请稍后再试";
    }

    //必须先开启 feign自带的hystrix  如 hystrix: enabled: true 否则配置的熔断方法类不起作用
    @GetMapping("testFeignsHystrix")
    public ResponseEntity<Spu> testFeignsHystrix(){
        Spu spu = goodsClient.querySpuById(23L);
        return  ResponseEntity.ok(spu);
    }
}
