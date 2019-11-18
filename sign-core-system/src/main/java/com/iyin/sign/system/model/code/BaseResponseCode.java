package com.iyin.sign.system.model.code;

/**
 * @ClassName:   BaseResponseCode
 * @Description: 通用错误码  0 1000 - 1999 通用异常码  其他业务系统错误码以1000的区间大小划分
 *  业务系统错误码不在此类中体现,各业务系统自己创建各自的错误常量类
 * @Author:      luwenxiong
 * @CreateDate:  2018/7/30 10:13
 * @UpdateUser:  luwenxiong
 * @UpdateDate:  2018/7/30 10:13
 * @Version:     0.0.1
 */
public enum BaseResponseCode implements ResponseCodeInterface{

    /**
     * 请求成功
     */
    SUCCESS(0, "请求成功","Success"),
    /**
     * 未知异常
     */
    OTHEREXCEPTION(2000, "未知异常","OtherException"),
    /**
     * 空指针异常
     */
    NULLPOINTEREXCEPTION(2001, "空指针异常","NullPointerException"),
    /**
     * 类型转换异常
     */
    CLASSCASTEXCEPTION(2002, "类型转换异常","ClassCastException"),
    /**
     * IO异常
     */
    IOEXCEPTION(2003, "IO异常","IOException"),
    /**
     * 未知方法异常
     */
    NOSUCHMETHODEXCEPTION(2004, "未知方法异常","NoSuchMethodException"),
    /**
     * 数组越界异常
     */
    INDEXOUTOFBOUNDSEXCEPTION(2005, "数组越界异常","IndexOutOfBoundsException"),

    /**
     * 方法参数校验异常
     */
    METHODARGUMENTNOTVALIDEXCEPTION(2006, "方法参数校验异常","MethodArgumentNotValidException"),

    /**
     * 服务调用异常
     */
    SERVICECALLEXCEPTION(2007, "服务调用异常","Service call exception"),

    /**
     * 系统繁忙，请稍候再试
     */
    SYSTEM_BUSY(2008, "系统繁忙，请稍候再试"),

    /**
     * 非法数据
     */
    ILLEGAL_ARGUMENT_EXCEPTION(2009, "非法数据"),

    DATE_PARSE_ERROR(3003, "时间转换异常", "date parse error"),

    ;

    /**
     * 错误码
     */
    private final int code;
    /**
     * 错误消息
     */
    private final String msg;

    /**
     * 错误消息（英文）
     */
    private final String enMsg;

    BaseResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.enMsg = "";
    }

    BaseResponseCode(int code, String msg, String enMsg) {
        this.code = code;
        this.msg = msg;
        this.enMsg = enMsg;
    }

    @Override
    public int getCode() {
        return code;
    }
    @Override
    public String getMsg() {
        return msg;
    }
    @Override
    public String getEnMsg() {
        return enMsg;
    }
}
