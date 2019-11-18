package com.iyin.sign.system.message;

import com.alibaba.fastjson.JSON;
import com.iyin.sign.system.util.MailToolUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName: MessageReceiver
 * @Description: 消息订阅
 * @Author: luwenxiong
 * @CreateDate: 2019/8/9 15:43
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/8/9 15:43
 * @Version: 0.0.1
 */
@Data
@Slf4j
public  class MessageSub {

    /**
     * 订阅的频道
     */
    public String channel;


    public MessageSub(String channel) {
        this.channel = channel;
    }

    /**接收消息的方法*/
     void handlerMessage(String msg){
         log.info("接受消息：{}",msg);
     }
}
