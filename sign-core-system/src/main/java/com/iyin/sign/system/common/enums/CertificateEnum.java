package com.iyin.sign.system.common.enums;

/**
 * @ClassName: CertificateEnum.java
 * @Description: 
 * @Author: yml
 * @CreateDate: 2019/6/27
 * @UpdateUser: yml
 * @UpdateDate: 2019/6/27
 * @Version: 1.0.0
 */
public enum CertificateEnum {
    /**
     * 单位证书库关联类型
     */
    ORG_CERT("01", "单位证书库关联"),
    /**
     * 在线软证书类型
     */
    ONLINE_CERT("02", "在线软证书"),
    /**
     * 本地生成证书类型
     */
    LOCAL_CERT("03", "本地生成证书");
    private String code;
    private String name;

    CertificateEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
