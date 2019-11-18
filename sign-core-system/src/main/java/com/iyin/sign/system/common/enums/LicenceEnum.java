package com.iyin.sign.system.common.enums;

/**
	* @Description LicenceEnum
	* @Author: wdf
    * @CreateDate: 2019/2/25 12:23
	* @UpdateUser: wdf
    * @UpdateDate: 2019/2/25 12:23
	* @Version: 0.0.1
    * @param
    * @return
    */
public enum LicenceEnum {
    /**
     * 成功
     */
    SUCCESS(0,"成功"),
    /**
     * 失败
     */
    ERROR(1,"失败"),

    /**
    	* @Description 是否使用时间校验
    	* @Author: wdf
        * @CreateDate: 2019/2/22 9:52
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/2/22 9:52
        */
    NOTTIME(0,"否"),ISTIME(1,"是"),
    /**
    	* @Description 是否使用次数校验
    	* @Author: wdf
        * @CreateDate: 2019/2/22 9:53
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/2/22 9:53
        */
    NOTCOUNT(0,"否"),ISCOUNT(1,"是"),
    /**
    	* @Description 0试用期,1永久有效,2时间和次数双重校验,3以有效时间为主,4以签章次数为主
    	* @Author: wdf
        * @CreateDate: 2019/2/22 10:10
    	* @UpdateUser: wdf
        * @UpdateDate: 2019/2/22 10:10
        */
    EFFECTIVEZERO(0,"试用期"),EFFECTIVEONE(1,"永久有效"),EFFECTIVETWO(2,"时间和次数"),EFFECTIVETHREE(3,"有效时间"),EFFECTIVEFOUR(4,"签章次数")
    ;
    private int code;

    private String name;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    LicenceEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
