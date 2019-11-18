package com.iyin.sign.system.common.enums;

/**
 * @ClassName: PictureTypeEnum
 * @Description: 图片文件类型枚举
 * @Author: luwenxiong
 * @CreateDate: 2018/8/14 10:16
 * @UpdateUser: luwenxiong
 * @UpdateDate: 2018/8/14 10:16
 * @Version: 0.0.1
 */
public enum PictureFormatEnum {
    /**
     * 01-BMP
     * 02-PNG
     * 03-JPG
     * */
    BMP("01", "BMP"),
    PNG("02", "PNG"),
    JPG("03", "JPG"),
    ;

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    PictureFormatEnum(String code , String name){
        this.code = code;
        this.name = name;
    }

    /**
     * 根据代码获取枚举名称
     * */
    public static String getNameByCode (String code){
        for (PictureFormatEnum pictureTypeEnum : PictureFormatEnum.values()){
            if (pictureTypeEnum.getCode().equals(code)){
                return pictureTypeEnum.getName();
            }
        }
        return null;
    }

    /**
     * 根据名称获取枚举代码
     * */
    public static String getCodeByName(String name){
        for (PictureFormatEnum pictureTypeEnum : PictureFormatEnum.values()){
            if (pictureTypeEnum.getName().equals(name)){
                return pictureTypeEnum.getCode();
            }
        }
        return null;
    }

    /**
     * 根据代码获取枚举对象
     * */
    public static PictureFormatEnum getPictureTypeEnumByCode(String code){
        for (PictureFormatEnum pictureTypeEnum : PictureFormatEnum.values()){
            if (pictureTypeEnum.getCode().equals(code)){
                return pictureTypeEnum;
            }
        }
        return null;
    }

    /**
     * 根据名称获取枚举对象
     * */
    public static PictureFormatEnum getPictureTypeEnumByName(String name){
        for (PictureFormatEnum pictureTypeEnum : PictureFormatEnum.values()){
            if (pictureTypeEnum.getName().equals(name)){
                return pictureTypeEnum;
            }
        }
        return null;
    }
}
