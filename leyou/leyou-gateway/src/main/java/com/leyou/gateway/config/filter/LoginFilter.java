package com.leyou.gateway.config.filter;

import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.JwtUtils;
import com.leyou.gateway.config.FilterProperties;
import com.leyou.gateway.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.ServerSocket;
import java.util.List;

@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})//也可以不用这个注解 但是要在所有属性配置类上加 @Component
public class LoginFilter extends ZuulFilter {

    @Autowired
    private  JwtProperties jwtProperties;
    @Autowired
    private FilterProperties filterProperties;
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        List<String> allowPaths = this.filterProperties.getAllowPaths();
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String url = request.getRequestURL().toString();
        for (String allowPath : allowPaths) {
             if(StringUtils.contains(url,allowPath)){
                 return  false;
             }
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //初始化运行上下文
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
          if(StringUtils.isBlank(token)){
              context.setSendZuulResponse(false);// 不转发
              context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
          }
        try {
            JwtUtils.getInfoFromToken(token,jwtProperties.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
            context.setSendZuulResponse(false);// 不转发
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value()/*其实就是401*/);
        }
        return null;
    }
}
