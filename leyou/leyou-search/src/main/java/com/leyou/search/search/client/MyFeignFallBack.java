package com.leyou.search.search.client;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xieduomou
 * @title: MyFeignFallBack
 * @projectName leyou
 * @description: 测试feign调用服务提供方失败时候的返回类 前提是要在 feign配置文件中开启feign自带的hystrix 例如hystrix: enabled: true
 * @date 2021/2/2120:00
 */
@Component
public class MyFeignFallBack implements GoodsClient{

    @Override
    public SpuDetail querySpuDetailById(Long spuId) {
        return null;
    }

    @Override
    public PageResult<SpuBo> querySpuBoBypage(String key, Boolean saleable, Integer page, Integer rows) {
        return null;
    }

    @Override
    public List<Sku> querySkusBySpuId(Long id) {
        return null;
    }
    /**
    　　* @description: 模拟feign远程调用失败 hystrix熔断   调用失败则会跳转到这个类 调用同名方法  重点必须现在yml文件中开启 feign自带的hystrix
    　　* @param [id]
    　　* @return com.leyou.item.pojo.Spu
    　　* @methodName querySpuById
    　　* @author Administrator
    　　* @date 2021/2/21 20:07
    　　*/
    @Override
    public Spu querySpuById(Long id) {
        Spu spu = new Spu();
        spu.setId(2L);
        return spu;
    }

    @Override
    public Sku querySkuBySkuId(Long skuId) {
        return null;
    }
}
