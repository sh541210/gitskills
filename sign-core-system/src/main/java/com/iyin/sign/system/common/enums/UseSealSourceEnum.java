package com.iyin.sign.system.common.enums;

/**
 * @ClassName: UseSealSourceEnum.java
 * @Description: 用印申请来源
 * @Author: yml
 * @CreateDate: 2019/10/10
 * @UpdateUser: yml
 * @UpdateDate: 2019/10/10
 * @Version: 1.0.0
 */
public enum UseSealSourceEnum {

    /**
     * 合同
     */
    CONTRACT(1, "合同"),

    /**
     * 文件
     */
    FILE(2, "文件"),

    /**
     * 用印申请
     */
    USE_SEAL(3, "用印申请");
    private Integer code;

    private String name;

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
    public static String getName(int code) {
        for (UseSealSourceEnum value : UseSealSourceEnum.values()) {
            if(value.code == code){
                return value.name;
            }
        }
        return UseSealSourceEnum.CONTRACT.name;
    }

    UseSealSourceEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
