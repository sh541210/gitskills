package com.iyin.sign.system.common.enums;

/**
 * 数据权限类型枚举
 * @Author:      luwenxiong
 * @CreateDate:  2019/8/21
 * @UpdateUser:
 * @UpdateDate:  2019/8/21
 * @Version:     0.0.1
 * @return
 * @throws
 */
public enum DataLimitTypeEnum {

    /**
     * 全部
     */
    ALL_DATA_LIMIT("0", "全部"),

    /**
     * 自己
     */
    MYSELF_DATA_LIMIT("1", "自己"),

    /**
     * 本级及子级
     */
    MYSELF_UNIT_SON_DATA_LIMIT("2","本级及子级");


    private String code;

    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    DataLimitTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
