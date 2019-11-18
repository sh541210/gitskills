package com.iyin.sign.system.message;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: MessageInfo
 * @Description: 消息类
 * @Author: luwenxiong
 * @CreateDate: 2019/8/9 15:46
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/8/9 15:46
 * @Version: 0.0.1
 */
@Data
public class MessageInfo implements Serializable {

    private static final long serialVersionUID = 6672568164595785330L;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "发生消息的具体内容")
    private String message;

    @ApiModelProperty(value = "消息发布的频道")
    private String channel;
}
