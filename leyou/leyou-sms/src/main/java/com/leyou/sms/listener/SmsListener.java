package com.leyou.sms.listener;

import com.aliyuncs.exceptions.ClientException;
import com.leyou.sms.config.SmsProperties;
import com.leyou.sms.ustils.SmsUtils;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Map;

@Component
public class SmsListener {
    @Autowired
    private SmsUtils smsUtils;
    @Autowired
    private SmsProperties smsProperties;

    @RabbitListener(bindings =@QueueBinding(
            value = @Queue(value = "leyou.sms.queue",durable = "true"),
            exchange = @Exchange(value = "leyou.sms.exchange",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"verifycode.sms"}
    ))
    public void sendSms(Map<String,String> msg) throws ClientException, IOException {
        if (CollectionUtils.isEmpty(msg)){
            return;
        }
        String phone=msg.get("phone");
        String code=msg.get("code");
       if( StringUtils.isNoneBlank(phone) && StringUtils.isNoneBlank(code)){
       this.smsUtils.sendSms(phone,code,smsProperties.getSignName(),smsProperties.getVerifyCodeTemplate());
       }
    }
}
