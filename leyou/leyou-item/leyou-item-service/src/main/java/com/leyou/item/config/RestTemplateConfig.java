package com.leyou.item.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author xieduomou
 * @title: RestTemplateConfig
 * @projectName leyou
 * @description: 为项目添加RestTemplate 实例注入 也可以直接写在启动类
 * @date 2021/2/2122:20
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced//开启ribbon负载均衡 使用了这个注解之后就不能用host加port方式当url了 只能用服务id当url例如：http://item-service/brand
    public RestTemplate restTemplate(){
        return  new RestTemplate();
    }

    //默认的resttemplate实现方式是urlconnection
    @Bean("urlConnection")
    public RestTemplate urlConnectionRestTemplate(){
        RestTemplate restTemplate = new RestTemplate(new SimpleClientHttpRequestFactory());
        return restTemplate;
    }

    @Bean("httpClient")
    public RestTemplate httpClientRestTemplate(){
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        return restTemplate;
    }

    @Bean("OKHttp3")
    public RestTemplate OKHttp3RestTemplate(){
        RestTemplate restTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory());
        return restTemplate;
    }
}
