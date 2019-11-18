package com.iyin.sign.system.common.enums;

/**
 * @ClassName SignatureEnum
 * LoginTypeEnum
 * @Author wdf
 * @Date 2019/3/7 16:38
 * @throws
 * @Version 1.0
 **/
public enum UserSourceEnum {

    /**
     * 用户创建类型注册创建
     */
    REGISTER_CREATE("0", "注册创建"),

    /**
     * 用户创建类型管理员创建
     */
    ADMIN_CREATE("01", "管理员创建");
    private String code;

    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    UserSourceEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
