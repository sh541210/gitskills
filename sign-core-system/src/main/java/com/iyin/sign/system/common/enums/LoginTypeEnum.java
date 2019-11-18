package com.iyin.sign.system.common.enums;

/**
 * @ClassName LoginTypeEnum
 * @Author wdf
 * @Date 2019/3/7 16:38
 * @throws
 * @Version 1.0
 **/
public enum LoginTypeEnum {
    /**
     * 登录类型手机号登录
     */
    PHONE_LOGIN("01", "手机号登录"),

    /**
     * 登录类型邮箱登录
     */
    EMAIL_LOGIN("02", "邮箱登录");
    private String code;

    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    LoginTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
