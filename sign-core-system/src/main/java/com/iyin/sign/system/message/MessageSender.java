package com.iyin.sign.system.message;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

/**
 * @ClassName: MessageSender
 * @Description: 消息发送
 * @Author: luwenxiong
 * @CreateDate: 2019/8/9 15:44
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/8/9 15:44
 * @Version: 0.0.1
 */
@Slf4j
@Component
public class MessageSender {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //@Scheduled(fixedRate = 2000) //间隔2s 通过StringRedisTemplate对象向redis消息队列chat频道发布消息
    public void sendMessage(MessageInfo messageInfo){
        String msg = JSON.toJSONString(messageInfo);
        log.info("转发消息 : {}", msg);
        //这个container 可以添加多个 messageListener
        stringRedisTemplate.convertAndSend("register",msg);
    }

}
