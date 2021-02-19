package com.tensquare.sms.listener;

import com.aliyuncs.exceptions.ClientException;
import com.tensquare.sms.util.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "sms")
public class SmsListener {

    //导入工具类
    @Autowired
    private SmsUtil smsUtil;

    //从yml自动拿去数据
    @Value("${aliyun.sms.sign_name}")
    private String sign_name;//签名

    @Value(("${aliyun.sms.template_code}"))
    private String template_code;//模板编号


    @RabbitHandler
    public void sendSms(Map<String,Object> map){
        String mobile = (String) map.get("mobile");
        String checkcode = (String) map.get("checkcode");

        //{"code":"验证码"}
        try {
            smsUtil.sendSms(mobile,template_code,sign_name,"{\"code\":\""+checkcode+"\"}");
        } catch (ClientException e) {
            e.printStackTrace();
        }

        System.out.println("手机号："+mobile);
        System.out.println("验证码："+checkcode);
    }
}
