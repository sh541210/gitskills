package com.iyin.sign.system.redisMq;

import com.alibaba.fastjson.JSONObject;
import com.iyin.sign.system.message.MessageSub;
import com.iyin.sign.system.message.MessageSender;
import com.iyin.sign.system.util.MailToolUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName: RedisMessageTest
 * @Description: redis消息中间件
 * @Author: luwenxiong
 * @CreateDate: 2019/8/9 15:51
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/8/9 15:51
 * @Version: 0.0.1
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RedisMessageTest {

    @Autowired
    MessageSender messageSender;

    @Autowired
    MessageSub messageReceiver;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    MailToolUtil mailToolUtil;


    @Test
    public void sendMessage(){

//        MessageInfo messageInfo = new MessageInfo();
//        messageInfo.setUserId("10014");
//        messageInfo.setMessage("我是测试消息");
//        messageSender.sendMessage(messageInfo);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email","504369985@qq.com");

//        log.info("用户注册频道收到一条消息：{}", jsonObject.toJSONString());
//        Map<String, String> map = JSON.parseObject(jsonObject.toJSONString(), Map.class);
//        String email = map.get("email");
//        log.info("email : {}", email);
//        try {
//            mailToolUtil.sendMail(email,"【签章系统注册提醒】","您"+email+"邮箱已成功注册");
//            log.info(email+"注册成功,成功发送注册邮件提醒");
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error(email+"监听器接受消息，邮件发送失败,异常信息:{}",e.getMessage());
//        }

        stringRedisTemplate.convertAndSend("register",jsonObject.toJSONString());
    }
}
