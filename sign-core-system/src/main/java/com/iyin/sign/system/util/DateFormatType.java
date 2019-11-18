package com.iyin.sign.system.util;

/**
 * 日期format类型
 *
 * @author: Koala
 * @Date: 14-8-22 下午5:03
 * @Version: 1.0
 */
public enum DateFormatType {
    /**
     * 年月yyyy-MM
     */
    MONTH("yyyy-MM"),
    /**
     * 年月yyyy-MM-dd
     */
    DATE("yyyy-MM-dd"),
    /**
     * 年月日yyyyMMdd
     */
    DATE_S("yyyyMMdd"),
    /**
     * 年月yyyyMM
     */
    DATA_Y("yyyyMM"),
    /**
     * 月日MMdd
     */
    DATE_MMdd("MMdd"),
    /**
     * 年月日时分秒yyyy-MM-dd HH:mm:ss
     */
    DATETIME("yyyy-MM-dd HH:mm:ss"),
    /**
     * 年月日时分秒yyyy-MM-dd HH:mm:ss
     */
    DATETIMESTR("yyyy年MM月dd日 HH:mm:ss"),
    /**
     * 年月日时分秒yyyyMMddHHmmss
     */
    DATETIME_S("yyyyMMddHHmmss");

    private String value;

    DateFormatType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
