package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    /**
     * 根据条件分页查询spu
     *
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuBoBypage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows
    ) {
        PageResult<SpuBo> result = this.goodsService.querySpuByPage(key, saleable, page, rows);
        if (result == null || CollectionUtils.isEmpty(result.getItems())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 新增商品
     *
     * @param spuBo
     * @return
     */
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo) {
        this.goodsService.saveGoods(spuBo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
        // return  ResponseEntity.ok().build();
    }

    /**
     * 更新商品
     *
     * @param spuBo
     * @return
     */
    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spuBo) {
        this.goodsService.updateGoods(spuBo);
        return ResponseEntity.noContent().build();
    }

    /**
     * 根据spuid查询spudetail
     *
     * @param spuId
     * @return
     */
    @GetMapping("spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> querySpuDetailById(@PathVariable("spuId") Long spuId) {
        SpuDetail spuDetail = this.goodsService.querySpuDetailById(spuId);
        if (spuDetail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spuDetail);
    }

    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkusBySpuId(@RequestParam("id") Long id) {
        List<Sku> skus = this.goodsService.querySkusBySpuId(id);
        if (CollectionUtils.isEmpty(skus)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(skus);
    }

    @GetMapping("{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id) {
        Spu spu = this.goodsService.querySpuByID(id);
        if (spu == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spu);
    }

    @GetMapping("sku/{skuId}")
    public ResponseEntity<Sku> querySkuBySkuId(@PathVariable("skuId") Long skuId) {

        Sku sku = this.goodsService.querySkuBySkuId(skuId);
        if (sku == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(sku);
    }
  //测试如何设置首页及 绑定多个路径 设置为 @RequestMapping 或者@RequestMapping("/")都是首页
 /*   @RequestMapping(value={"/","123"})
    @ResponseBody
    public String test(){
        return "index";

    }*/
 /**
 　　* @description: 测试 controller 返回xml格式数据而不是json
 　　* @param [ ]
 　　* @return com.leyou.item.pojo.Spu
 　　* @methodName
 　　*/
  @RequestMapping(value = "testXml",produces = "application/xml")
  @ResponseBody
  public Spu testXml(){
      Spu spu = new Spu();
      spu.setTitle("testXml");
      return  spu;
  }
}
