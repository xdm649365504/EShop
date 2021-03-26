package com.leyou.cart.interceptor;

import com.leyou.cart.config.JwtProperties;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.JwtUtils;
import com.leyou.common.utils.RsaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
@EnableConfigurationProperties(JwtProperties.class)
public class LoginInterceptor  extends HandlerInterceptorAdapter {

     private static final ThreadLocal<UserInfo> threadLocal = new ThreadLocal<>();

     @Autowired
     private JwtProperties jwtProperties;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从cookie中获取token
        String token   = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());
        //获取 userinfo
        UserInfo userInfo = JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());
         if (userInfo == null){
             return false;
         }
         //把userinfo放入线程局部变量
        threadLocal.set(userInfo);

         return true;
    }
    public static UserInfo getUserInfo(){
        return threadLocal.get();

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //释放资源 因为tomcat的线程池线程不会结束 不会自动清除
        threadLocal.remove();
    }
}
