package com.iyin.sign.system.common.enums;

/**
 * @ClassName: PictureTypeEnum
 * @Description: 章莫类型枚举
 * @Author: luwenxiong
 * @CreateDate: 2018/8/14 10:16
 * @UpdateUser: luwenxiong
 * @UpdateDate: 2018/8/14 10:16
 * @Version: 0.0.1
 * 01 ukey印章，02云签名，03 云印章，04ukey签名
 */
public enum PictureTypeEnum {

    /**
     * 印章图片类型
     */
    CLOUD_SIGN("01", "云印章"),
    UKEY_SEAL("02", "ukey印章"),
    CLOUD_SEAL("03", "云印章"),
    UKEY_SIGN("04","ukey签名")
    ;

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    PictureTypeEnum(String code , String name){
        this.code = code;
        this.name = name;
    }

    /**
     * 根据代码获取枚举名称
     * */
    public static String getNameByCode (String code){
        for (PictureTypeEnum pictureTypeEnum : PictureTypeEnum.values()){
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
        for (PictureTypeEnum pictureTypeEnum : PictureTypeEnum.values()){
            if (pictureTypeEnum.getName().equals(name)){
                return pictureTypeEnum.getCode();
            }
        }
        return null;
    }

    /**
     * 根据代码获取枚举对象
     * */
    public static PictureTypeEnum getPictureTypeEnumByCode(String code){
        for (PictureTypeEnum pictureTypeEnum : PictureTypeEnum.values()){
            if (pictureTypeEnum.getCode().equals(code)){
                return pictureTypeEnum;
            }
        }
        return null;
    }

    /**
     * 根据名称获取枚举对象
     * */
    public static PictureTypeEnum getPictureTypeEnumByName(String name){
        for (PictureTypeEnum pictureTypeEnum : PictureTypeEnum.values()){
            if (pictureTypeEnum.getName().equals(name)){
                return pictureTypeEnum;
            }
        }
        return null;
    }
}
