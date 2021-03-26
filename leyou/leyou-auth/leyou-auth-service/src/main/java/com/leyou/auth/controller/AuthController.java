package com.leyou.auth.controller;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.sercive.AuthService;

import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired@
    private AuthService authService;
    @Autowired
    private JwtProperties jwtProperties;
    @PostMapping("accredit")
    public ResponseEntity<Void> accredit (HttpServletRequest request, HttpServletResponse response, @RequestParam("username")String username, @RequestParam("password")String password){
      String token  = this.authService.accredit(username,password);
         if(StringUtils.isBlank(token)){
             return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
         }
        CookieUtils.setCookie(request,response,this.jwtProperties.getCookieName(),token, jwtProperties.getExpire()*60);
        return ResponseEntity.ok(null);
    }
    @GetMapping("verify")
    public  ResponseEntity<UserInfo> verify(HttpServletRequest request, HttpServletResponse response,
            @CookieValue("LY_TOKEN")String token){
        try {
            // 使用jwt 工具类使用公钥解密user信息
            UserInfo user = JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());
            if(user == null){
                return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            //刷新jwt中有效时间
            token= JwtUtils.generateToken(user,jwtProperties.getPrivateKey(),jwtProperties.getExpire());
            //刷新 cookie中有效时间
            CookieUtils.setCookie(request,response,jwtProperties.getCookieName(),token,jwtProperties.getExpire()*60);

            return  ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}

