package com.leyou.upload.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class LeyouCorsConfiguration {

    @Bean
    public CorsFilter corsFilter(){
           //初始化cors配置对象
        CorsConfiguration configuration = new CorsConfiguration();
        //允许跨域的域名 如果要带cookie 不能写*  *代表所有域名允许跨域
        configuration.addAllowedOrigin("http://manage.leyou.com");// 允许携带cookie
        configuration.setAllowCredentials(true);
        configuration.addAllowedMethod("*");//代表允许所有的请求方法 put get post head delete
        configuration.addAllowedHeader("*");//允许携带所有头信息

        //初始化cors配置源对象
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**",configuration);
        return new CorsFilter(configurationSource);
    }
}
