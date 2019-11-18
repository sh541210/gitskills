package com.iyin.sign.system.common.enums;

/**
 * @ClassName SignatureEnum
 * SignatureEnum
 * @Author wdf
 * @Date 2019/3/7 16:38
 * @throws
 * @Version 1.0
 **/
public enum SignatureEnum {
    /**
     * 签章种类：快捷签
     */
    SIGNATURE_FAST("02", "快捷签"),
    /**
     * 签章种类：UKey签
     */
    SIGNATURE_U_KEY("05", "UKey签"),

    /**
     * 签章方式：关键字签章
     */
    SIGNATURE_KEYWORD("1", "关键字签章"),
    /**
     * 签章方式：坐标签章
     */
    SIGNATURE_COORDINATE("2", "坐标签章"),

    /**
     * 签章类型：单页签章
     */
    SIGNATURE_SINGLE("01", "单页签章"),
    SINGLE("single", "单页签章"),
    /**
     * 签章类型：多页签章
     */
    SIGNATURE_MORE("02", "多页签章"),
    BATCH("batch", "多页签章"),
    /**
     * 签章类型：骑缝签章
     */
    SIGNATURE_PERFORATION("03", "骑缝签章"),
    PERFORATION("perforation", "骑缝签章"),
    /**
     * 签章类型：连页签章
     */
    SIGNATURE_CONTINUOUS("04", "连页签章"),

    SIGNATURE_PERFORATION_LEFT("0", "骑缝签章左"),
    SIGNATURE_PERFORATION_RIGHT("1", "骑缝签章右"),

    /**
     * 签章结果：成功
     */
    SIGNATURE_SUCCESS("01", "签章成功"),
    /**
     * 签章结果：失败
     */
    SIGNATURE_FAILURE("02", "签章失败");
    private String code;

    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    SignatureEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
