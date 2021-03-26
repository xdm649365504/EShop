package com.leyou.gateway.config;

import org.springframework.http.HttpMethod;
import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class LeyouCorsConfiguration {

    @Bean
    public CorsFilter corsFilter(){
           //初始化cors配置对象
        CorsConfiguration configuration = new CorsConfiguration();
        //允许跨域的域名 如果要带cookie addAllowedOrigin()不能写*  *代表所有域名允许跨域
        // 用* 也可以携带cookie 写*的情况下前段ajxa需要加{withCredentials: true}才可以携带cookie
        //或者在axios中设置 axios.defaults.withCredentials = true
        configuration.addAllowedOrigin("http://manage.leyou.com");// 允许携带cookie
        configuration.addAllowedOrigin("http://www.leyou.com");// 允许携带cookie
        configuration.addAllowedOrigin("http://ja29193368.zicp.vip");// 花生壳内网穿透映射的地址允许携带cookie
        configuration.setAllowCredentials(true);
        //设置跨域的时候一定要允许 options 方式的请求 或者设置为* 因为跨域请求都会预先发一次
        // options请求查看服务器信息 来看是否允许跨域 然后再发 原先的请求
        configuration.addAllowedMethod("*");//代表允许所有的请求方法 put get post head delete
      /*  configuration.addAllowedMethod(HttpMethod.GET);//代表允许所有的请求方法 put get post head delete
        configuration.addAllowedMethod(HttpMethod.PUT);//代表允许所有的请求方法 put get post head delete
        configuration.addAllowedMethod(HttpMethod.DELETE);//代表允许所有的请求方法 put get post head delete
        configuration.addAllowedMethod(HttpMethod.OPTIONS);//代表允许所有的请求方法 put get post head delete*/
        configuration.addAllowedHeader("*");//允许携带所有头信息

        //初始化cors配置源对象
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**",configuration);
        return new CorsFilter(configurationSource);
    }
}
