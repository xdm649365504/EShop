package com.leyou.cart.config;

import com.leyou.cart.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LeyouWebMvcConfiguration  implements WebMvcConfigurer
{
    @Autowired
    private  LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(loginInterceptor);
        // 添加拦截路径
         registration.addPathPatterns("/**");
    }
    /**
    　　* @description: 练习配置过滤器 重点是 urlpattern 配置
    　　* @param []
    　　* @return org.springframework.boot.web.servlet.FilterRegistrationBean
    　　* @methodName addFIlter
    　　* @author Administrator
    　　* @date 2021/2/23 12:25
    　　*/
   /*  @Bean
     public FilterRegistrationBean addFIlter(){
         FilterRegistrationBean frBean = new FilterRegistrationBean();
         frBean.setFilter(new Myfilter());
         frBean.addUrlPatterns("/");
         return  frBean;
     }*/
}
