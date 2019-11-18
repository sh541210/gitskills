package com.iyin.sign.system.message;

import com.alibaba.fastjson.JSON;
import com.iyin.sign.system.util.MailToolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName: MessageReceiver
 * @Description: 消息接收者
 * @Author: luwenxiong
 * @CreateDate: 2019/8/9 15:43
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/8/9 15:43
 * @Version: 0.0.1
 */
@Slf4j
@Component
public class MessageReceiver {

    @Autowired
    MailToolUtil mailToolUtil;

    /**接收消息的方法*/
    public void receiveMessage(String msg){
        log.info("收到一条消息：{}", msg);
        Map<String, String> map = JSON.parseObject(msg, Map.class);
        String sessionId = map.get("sessionId");
        String message = map.get("message");
        log.info("sessionId : {}", sessionId);
        log.info("message : {}", message);

        //todo:注册成功发送电子邮件


    }
}
