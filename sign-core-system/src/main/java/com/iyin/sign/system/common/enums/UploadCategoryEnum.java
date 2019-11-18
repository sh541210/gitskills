package com.iyin.sign.system.common.enums;

/**
 * @ClassName: UploadCategoryEnum
 * @Description: 白鹤上传文件业务
 * @Author: yml
 * @CreateDate: 2019/10/23
 * @UpdateUser: yml
 * @UpdateDate: 2019/10/23
 * @Version: 1.0.0
 */
public enum UploadCategoryEnum {

    /**
     * 白鹤上传文件业务
     */
    HEAD(1, "头像"),
    SIGNATURE(2, "签名"),
    SEAL(3, "印模"),
    FILE(4, "申请文件，申请图片，盖章后文件"),
    PICTURE(5, "盖章拍照图片"),
    ;

    private Integer code;

    private String name;

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    UploadCategoryEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
