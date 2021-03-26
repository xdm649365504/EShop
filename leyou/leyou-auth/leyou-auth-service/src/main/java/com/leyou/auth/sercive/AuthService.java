package com.leyou.auth.sercive;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JwtUtils;
import com.leyou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//@EnableConfigurationProperties(JwtProperties.class)  //只需注入一次就行
@Service
public class AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties jwtProperties;

    public String accredit(String username, String password) {
        //1.根据用户名密码查询
        User user = this.userClient.query(username, password);
        //2.判断user
        if (user == null) {
            return null;
        }
        //3.jwtUtils生成token

        try {
           String token = JwtUtils.generateToken(new UserInfo(user.getId(), user.getUsername()), jwtProperties.getPrivateKey(), jwtProperties.getExpire());
            return  token;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
