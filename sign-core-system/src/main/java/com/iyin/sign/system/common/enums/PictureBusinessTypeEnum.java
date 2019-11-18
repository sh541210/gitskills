package com.iyin.sign.system.common.enums;

/**
 * @ClassName: PictureTypeEnum
 * @Description: 章莫业务类型枚举
 * @Author: luwenxiong
 * @CreateDate: 2018/8/14 10:16
 * @UpdateUser: luwenxiong
 * @UpdateDate: 2018/8/14 10:16
 * @Version: 0.0.1
 * 01 ukey印章，02云签名，03 云印章，04ukey签名
 */
public enum PictureBusinessTypeEnum {

    /**
     * 单位的
     */
    ADMINISTRATION_SEAL("01", "行政章"),
    FINANCE_SEAL("02", "财务章"),
    BUSINESS_SPECIAL("03","业务专用章"),
    FANREN_REPRESENT("04","法人代表人名章"),
    CUSTOMS_SPECIAL("05","报关专用章"),
    CONTRACT_SPECIAL("06","合同专用章"),
    OTHERS_SEAL("07","其他公章"),
    ESEAL_MIXED("08","电子杂章"),
    ESEAL_PRIVATE("09","电子私章")
    ;

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    PictureBusinessTypeEnum(String code , String name){
        this.code = code;
        this.name = name;
    }

    /**
     * 根据代码获取枚举名称
     * */
    public static String getNameByCode (String code){
        for (PictureBusinessTypeEnum pictureTypeEnum : PictureBusinessTypeEnum.values()){
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
        for (PictureBusinessTypeEnum pictureTypeEnum : PictureBusinessTypeEnum.values()){
            if (pictureTypeEnum.getName().equals(name)){
                return pictureTypeEnum.getCode();
            }
        }
        return null;
    }

    /**
     * 根据代码获取枚举对象
     * */
    public static PictureBusinessTypeEnum getPictureTypeEnumByCode(String code){
        for (PictureBusinessTypeEnum pictureTypeEnum : PictureBusinessTypeEnum.values()){
            if (pictureTypeEnum.getCode().equals(code)){
                return pictureTypeEnum;
            }
        }
        return null;
    }

    /**
     * 根据名称获取枚举对象
     * */
    public static PictureBusinessTypeEnum getPictureTypeEnumByName(String name){
        for (PictureBusinessTypeEnum pictureTypeEnum : PictureBusinessTypeEnum.values()){
            if (pictureTypeEnum.getName().equals(name)){
                return pictureTypeEnum;
            }
        }
        return null;
    }
}
