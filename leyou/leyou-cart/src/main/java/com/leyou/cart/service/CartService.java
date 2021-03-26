package com.leyou.cart.service;

import com.leyou.cart.client.GoodsClinet;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Sku;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private static final String KEY_PREFIX = "user:cart";

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private GoodsClinet goodsClinet;

    public void addCart(Cart cart) {
        //获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();

        //查询购物车记录
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        String key = cart.getSkuId().toString();
        Integer num = cart.getNum();
        //判断当前商品是否在购物车中
        if (hashOps.hasKey(key)){
            //在 更新数量
            String cartJson = hashOps.get(key).toString();
            cart = JsonUtils.parse(cartJson, Cart.class);
            cart.setNum(cart.getNum() + num);
        }
        else{
            //不在 新增购物车
            Sku sku = this.goodsClinet.querySkuBySkuId(cart.getSkuId());
            cart.setUserId(userInfo.getId());
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setTitle(sku.getTitle());
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
            cart.setPrice(sku.getPrice());
        }
        hashOps.put(key, JsonUtils.serialize(cart));

    }

    public List<Cart> queryCarts() {
        UserInfo userInfo = LoginInterceptor.getUserInfo();
         if(!this.redisTemplate.hasKey(KEY_PREFIX+userInfo.getId())){
             return null;
         }
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        List<Object> cartJsons = hashOps.values();
        if(CollectionUtils.isEmpty(cartJsons)){
            return  null;
        }
        //把List<object> 转为 list<Cart>
        return   cartJsons.stream().map(cartJson->/*省略return*/JsonUtils.parse(cartJson.toString(),Cart.class)).collect(Collectors.toList());

    }

    public void updateNum(Cart cart) {

        UserInfo userInfo = LoginInterceptor.getUserInfo();
        if(!this.redisTemplate.hasKey(KEY_PREFIX+userInfo.getId())){
            return ;
        }
        Integer num= cart.getNum();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());

        String cartJson = hashOps.get(cart.getSkuId().toString()).toString();

        cart = JsonUtils.parse(cartJson, Cart.class);
        cart.setNum(num);
        hashOps.put(cart.getSkuId().toString(),JsonUtils.serialize(cart));
    }

    public void deleteCart(String skuId) {
        // 获取登录用户
        UserInfo user = LoginInterceptor.getUserInfo();
        String key = KEY_PREFIX + user.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        hashOps.delete(skuId);
    }
}
