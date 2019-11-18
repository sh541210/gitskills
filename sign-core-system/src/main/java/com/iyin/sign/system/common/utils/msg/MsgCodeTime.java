package com.iyin.sign.system.common.utils.msg;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: MsgCodeTime
 * @Description: 验证码使用次数信息类
 * @Author: 刘志
 * @CreateDate: 2018/9/18 10:03
 * @UpdateUser: 刘志
 * @UpdateDate: 2018/9/18 10:03
 * @Version: 0.0.1
 */
@Data
public class MsgCodeTime implements Serializable{

    private static final long serialVersionUID = 4605458953751960933L;
    /**
     * 获取验证码次数
     */
    private Integer times;
    /**
     * 获取验证码日期
     */
    private Date dates;
    /**
     * 是否锁定
     */
    private Boolean lock;

}
