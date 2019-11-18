package com.iyin.sign.system.common.enums;

/**
 * @ClassName TemplateEnum
 * TemplateEnum
 * @Author wdf
 * @Date 2019/8/13 20:00
 * @throws
 * @Version 1.0
 **/
public enum TemplateEnum {

    /**
     * word
     * @author wdf
     * @date 2019/8/13
    */
    TEMP_WORD("1","WORD"),
    /**
     * html
     * @author wdf
     * @date 2019/8/13
     */
    TEMP_HTML("2","HTML"),
    /**
     * pdf
     */
    TEMP_PDF("3","PDF");

    private String code;

    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    TemplateEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
