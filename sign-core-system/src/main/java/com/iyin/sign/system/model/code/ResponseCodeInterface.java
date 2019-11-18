package com.iyin.sign.system.model.code;

/**
 * @ClassName:   ResponseCodeInterface
 * @Description: 错误码接口
 * @Author:      luwenxiong
 * @CreateDate:  2018/8/2 16:07
 * @UpdateUser:  luwenxiong
 * @UpdateDate:  2018/8/2 16:07
 * @Version:     0.0.1
 */
public interface ResponseCodeInterface {

    /**
     * 获取编码
     * @return
     */
    int getCode();

    /**
     * 获取编码信息
     * @return
     */
    String getMsg();

    /**
     * 获取编码英文信息
     * @return
     */
    String getEnMsg();
}
