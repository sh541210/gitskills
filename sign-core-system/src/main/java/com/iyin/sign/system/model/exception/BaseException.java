package com.iyin.sign.system.model.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName: BaseException
 * @Description: 基础异常
 * @Author: 肖泛舟
 * @CreateDate: 2018/7/28 11:12
 * @UpdateUser: 肖泛舟
 * @UpdateDate: 2018/7/28 11:12
 * @Version: 0.0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 906670803351818092L;

    /**
     * 异常编号
     */
    private final int messageCode;

    /**
     * 对messageCode 异常信息进行补充说明
     */
    private final String detailMessage;

    public int getMessageCode() {
        return messageCode;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public BaseException(int messageCode) {
        this.messageCode = messageCode;
        this.detailMessage = "";
    }

    public BaseException(int messageCode, String message) {
        super(message);
        this.messageCode = messageCode;
        this.detailMessage = message;
    }

    public BaseException(int messageCode, String message, String detailMessage) {
        super(message);
        this.messageCode = messageCode;
        this.detailMessage = detailMessage;
    }

    @Override
    public String toString() {
        return String.format("BaseException [messageCode=%s ] [detailMessage=%s ]", messageCode, detailMessage);
    }


}