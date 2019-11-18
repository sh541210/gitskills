package com.iyin.sign.system.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName UserLicenceInfo
 * UserLicenceInfo
 * @Author wdf
 * @Date 2019/8/16 15:15
 * @throws
 * @Version 1.0
 **/
@Data
public class UserLicenceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "版本信息")
    private String versionInfo;

    @ApiModelProperty(value = "序列号")
    private String serialNumber;

    @ApiModelProperty(value = "是否有效")
    private Integer isEffective;

    @ApiModelProperty(value = "产品类型")
    private String sysType;

    @ApiModelProperty(value = "授权单位")
    private String userEn;

    @ApiModelProperty(value = "产品名称")
    private String userName;

    @ApiModelProperty(value = "mac地址")
    private String userMac;

    @ApiModelProperty(value = "有效期")
    private String dateTime;
}
