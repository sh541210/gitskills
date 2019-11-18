package com.iyin.sign.system.common.enums;

/**
	* @Description
	* @Author: wdf
    * @CreateDate: 2018/12/25 12:22
	* @UpdateUser: wdf
    * @UpdateDate: 2018/12/25 12:22
	* @Version: 0.0.1
    * @param
    * @return
    */
public enum AppEnum {
    /**
     * 成功
     */
    SUCCESS(0,"成功"),
    /**
     * 失败
     */
    ERROR(1,"失败"),

    UNDELETE(0,"未删除"),DELETE(1,"已删除"),
    UNTYPE(2,"未回收"),TYPE(1,"已回收"),
    UNSUSPEND(0,"未停用"),SUSPEND(1,"已停用"),
    TYPEFILE(0,"文件流"),TYPEBASE64(1,"base64"),
    /**
     * 应用平台类型：1移动端；2网页端；3客户端；
     */
    PLATFORMONE(1,"移动端"),
    PLATFORMTWO(2,"网页端"),
    PLATFORMTHREE(3,"客户端"),
    USERMANAGER(1,"后台用户"),USERAPI(2,"前台用户"),
    PERSON(1,"个人"),ENTERPRISE(2,"企业")
    ;
    private int code;

    private String name;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    AppEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
