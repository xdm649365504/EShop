package com.leyou.search.search.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service",decode404 = true,fallback = MyFeignFallBack.class)
//@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {

}
