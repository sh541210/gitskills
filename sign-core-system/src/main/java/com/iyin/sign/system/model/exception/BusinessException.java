package com.iyin.sign.system.model.exception;


import com.iyin.sign.system.model.code.ResponseCodeInterface;

/**
 * @ClassName: BusinessException
 * @Description: 基础业务异常
 * @Author: luwenxiong
 * @CreateDate: 2018/7/28 11:12
 * @UpdateUser: luwenxiong
 * @UpdateDate: 2018/7/28 11:12
 * @Version: 0.0.1
 */
public class BusinessException extends BaseException {

    private static final long serialVersionUID = 906670803351818092L;

    public BusinessException(int messageCode) {
        super(messageCode);
    }

    public BusinessException(int messageCode, String detailMessage) {
        super(messageCode, detailMessage);
    }

    public BusinessException(String messageCode, String detailMessage) {
        super(Integer.parseInt(messageCode), detailMessage);
    }

    /**
     * 构造函数
     * @param messageCode
     * @param detailMessage
     * @param throwable 保存引发异常的堆栈
     */
    public BusinessException(int messageCode, String detailMessage, Throwable throwable){
        super(messageCode,detailMessage);
        this.setStackTrace(throwable.getStackTrace());
    }

    /**
     * 构造函数
     * @param code 异常码
     */
    public BusinessException(ResponseCodeInterface code) {
        super(code.getCode(), code.getMsg());
    }

    /**
     * 构造函数
     * @param code 异常码
     * @param throwable 保存引发异常的堆栈
     */
    public BusinessException(ResponseCodeInterface code, Throwable throwable){
        super(code.getCode(), code.getMsg());
        this.setStackTrace(throwable.getStackTrace());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}