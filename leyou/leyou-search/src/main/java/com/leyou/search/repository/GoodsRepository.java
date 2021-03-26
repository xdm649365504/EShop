package com.leyou.search.repository;

import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {

//    List<Goods>  findBySubTitle(String subTitle);// 此方法错误 因为设置了SubTitle 的 index为false
      List<Goods>  findByAll(String text);
}
