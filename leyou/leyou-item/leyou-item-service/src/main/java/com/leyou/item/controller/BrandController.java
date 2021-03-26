package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import lombok.Cleanup;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService brandService;
    @Autowired
    private RestTemplate restTemplate;
    /**
     *根据查询条件分页查询品牌信息
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandsByPage(
            @RequestParam(value="key",required = false)String key,
            @RequestParam(value="page",defaultValue = "1")Integer page,
            @RequestParam(value="row",defaultValue = "5")Integer rows,
            @RequestParam(value="sortBy",required = false)String sortBy,
            @RequestParam(value="desc",required = false)Boolean desc
    ){
        PageResult<Brand> result= brandService.queryBrandsBypage(key,page,rows,sortBy,desc);
        if(CollectionUtils.isEmpty(result.getItems())) {
            return ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(result);
    }

    @PostMapping
    public  ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids")List<Long> cids){
         brandService.saveBrand(brand,cids);
         return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping("test")
    public  ResponseEntity<Void> test(){
        System.out.println(123);
        return  ResponseEntity.ok(null);
    }
    /**
    　　* @description: 在一个controller方法中利用RestTemplate请求另外一个方法 可以获取返回值
                          以前能想到的方法是 return redirect 和 return forward
     Spring提供了一个RestTemplate模板工具类，对基于Http的客户端进行了封装，并且实现了对象与json的序列化和反序列化，非常方便。RestTemplate并没有限定Http的客户端类型，而是进行了抽象，目前常用的3种都有支持：
     HttpClient
     OkHttp
     JDK原生的URLConnection（默认的）
    　　* @param [cid]
    　　* @return org.springframework.http.ResponseEntity<java.util.List>
    　　* @methodName testRest
    　　* @author Administrator
    　　* @date 2021/2/21 19:09
    　　*/
    @GetMapping("testRest/{cid}")
    public ResponseEntity<List> testRest(@PathVariable("cid") Long cid) throws URISyntaxException {
        //在一个controller方法中利用RestTemplate请求另外一个方法 可以获取返回值 以前能想到的方法是 return redirect 和 return forward
//        ResponseEntity<List> list = restTemplate.getForEntity("http://localhost:8081/brand/cid/{cid}", List.class, cid);
        //因为使用了@loadBalanced开启了ribbon负载均衡所以这个 resttemplate不能使用主机加端口请求其他服务了只能用eureka服务ID
//        URI uri = new URIBuilder().setScheme("http").setHost("localhost").setPort(8081).setPath("/brand/cid/" + cid).build();
        List list = restTemplate.getForObject("http://item-service/brand/cid/" + cid, List.class);
        return ResponseEntity.ok(list);
    }

    /**
     * 根据分类id 查询品牌列表
     * @param cid
     * @return
     */
    @GetMapping("cid/{cid}")
    public  ResponseEntity<List<Brand>> quertBrandsByid(@PathVariable(value="cid")Long cid){

        List<Brand> brands=this.brandService.queryBrandsByCid(cid);
            if(CollectionUtils.isEmpty(brands)){
            return ResponseEntity.notFound().build();
            }
         return  ResponseEntity.ok(brands);
    }

/*    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id")Long id){
        Brand brand=this.brandService.querBrandById(id);
        if(brand==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brand);
    }*/
        @GetMapping("{id}")
        public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id){
            return ResponseEntity.ok(brandService.querBrandById(id));
        }

}
