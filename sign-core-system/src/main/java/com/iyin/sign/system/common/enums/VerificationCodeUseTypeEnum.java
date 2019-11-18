package com.iyin.sign.system.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @ClassName: VerificationCodeUseTypeEnum
 * @Description: 验证码使用类型枚举
 * @Author: 刘志
 * @CreateDate: 2018/9/17 16:58
 * @UpdateUser: 刘志
 * @UpdateDate: 2018/9/17 16:58
 * @Version: 0.0.1
 */
public enum VerificationCodeUseTypeEnum {
    /**
     * REGISTER注册
     */
    REGISTER("1","REGISTER_ACCOUNT"),
    /**
     * UPDATE修改密码
     */
    UPDATE("2","UPDATE_PASSWORD"),
    /**
     * FIND找回密码
     */
    FIND("3","FIND_PASSWORD"),
    /**
     * 设置或修改签署密码
     */
    SET_OR_UPDATE_SIGN_PWD("4","SET_OR_UPDATE_SIGN_PWD"),
    /**
     * ukey初始化
     */
    UKEY_INIT("5","UKEY_INIT"),
    /**
     * 修改邮箱
     */
    UPDATE_EMAIL("6","UPDATE_EMAIL"),
    /**
     * 修改手机号
     */
    UPDATE_PHONE("7","UPDATE_PHONE"),
    /**
     * 修改地址
     */
    UPDATE_ADDRES("8","UPDATE_ADDRES"),
    /**
     * UKEY 挂失
     */
    UKEY_LOSS("9","UKEY_LOSS"),
    /**
     * ukey 取消挂失
     */
    UKEY_LOSS_CANCEL("10","UKEY_LOSS_CANCEL"),

    /**
     * 解绑手机号码
     */
    UNBIND_PHONE("11","UNBIND_PHONE"),

    /**
     * 绑定手机号码
     */
    BIND_PHONE("12","BIND_PHONE"),

    /**
     * 合同文件下载验证身份
     */
    COMPACT_DOWNLOAD_VALIDATE("13","COMPACT_DOWNLOAD_VALIDATE"),

    /**
     * 安印通用户注册
     */
    IYIN_APP_REGISTER("14","IYIN_APP_REGISTER"),

    /**
     * 安印通用户注册
     */
    IYIN_APP_RESET_PWD("15","IYIN_APP_RESET_PWD"),

    /**
     * 安印云签用户忘记密码
     */
    IYIN_CLOUD_FORGET_PWD("31","IYIN_APP_RESET_PWD"),
    ;
    private String code;

    private String name;

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    VerificationCodeUseTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据代码获取枚举名称
     */
    public static String getNameByCode(String code) {
        for (VerificationCodeUseTypeEnum thisEnum: VerificationCodeUseTypeEnum.values()) {
            if (thisEnum.getCode().equals(code)) {
                return thisEnum.getName();
            }
        }
        return null;
    }

    /**
     * 根据名称获取枚举代码
     */
    public static String getCodeByName(String name) {
        for (VerificationCodeUseTypeEnum thisEnum: VerificationCodeUseTypeEnum.values()) {
            if (thisEnum.getName().equals(name)) {
                return thisEnum.getCode();
            }
        }
        return null;
    }


}
