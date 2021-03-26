package com.leyou.search.search.client;

import com.leyou.item.api.BrandApi;
import com.leyou.item.pojo.Brand;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "item-service",url = "http://127.0.0.1:8081")
@FeignClient("item-service")
public interface BrandClient  {
    @GetMapping("brand/{id}")
    Brand queryBrandById(@PathVariable("id") Long id);
}
