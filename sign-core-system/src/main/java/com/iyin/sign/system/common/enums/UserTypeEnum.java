package com.iyin.sign.system.common.enums;

/**
 * @ClassName SignatureEnum
 * UserTypeEnum
 * @Author wdf
 * @Date 2019/3/7 16:38
 * @throws
 * @Version 1.0
 **/
public enum UserTypeEnum {

    /**
     * 用户类型单位用户
     */
    ENTERPRISE_USER("01", "单位用户"),

    /**
     * 用户类型个人用户
     */
    PERSONAL_USER("02", "个人用户");
    private String code;

    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    UserTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
