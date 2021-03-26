package com.leyou.user.service;

import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.utils.CodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;
    private static final String KEY_PREFIX = "user:verify:";

    /**
     * 校验数据是否可用
     *
     * @param data
     * @param type
     * @return
     */
    public Boolean checkUser(String data, Integer type) {
        User record = new User();
        if (type == 1) {
            record.setUsername(data);
        } else if (type == 2) {
            record.setPhone(data);
        } else {
            return null;
        }
        return this.userMapper.selectCount(record) == 0;
    }

    public void sendVerifyCode(String phone) {
        if (StringUtils.isBlank(phone)) {
            return;
        }
        //生成验证码
        String code = NumberUtils.generateCode(6);
        Map<String, String> msg = new HashMap<>();
        msg.put("phone", phone);
        msg.put("code", code);
        //发送到rabbitMq
        this.amqpTemplate.convertAndSend("leyou.sms.exchange", "verifycode.sms", msg);
        //保存验证码到redis 设置过期时间

        this.redisTemplate.opsForValue().set(KEY_PREFIX + phone, code, 5, TimeUnit.MINUTES);

    }

    public boolean register(User user, String code) {
        // 查询redis中对应的验证码
        String redisCode = this.redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        //校验验证码
        if (!StringUtils.equals(code, redisCode)) {
            return false;
        }
        //生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        //加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));
        //新增用户
        user.setId(null);
        user.setCreated(new Date());
        boolean bool = this.userMapper.insertSelective(user) == 1;
        if (bool) {
            redisTemplate.delete(KEY_PREFIX + user.getPhone());
        }
        return bool;
    }

    public User queryUser(String username, String password) {
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        if (user == null) {
            return null;
        }
        //获取盐 进行加密
        String salt = user.getSalt();
        password = CodecUtils.md5Hex(password, salt);
        // 和数据库密码进行比较
        if (StringUtils.equals(password, user.getPassword())) {
            return user;
        } else {
            return null;
        }

    }
}
