package com.iyin.sign.system.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
	* 证书信息
	* @Author: wdf
    * @CreateDate: 2019/7/23 12:00
	* @UpdateUser: wdf
    * @UpdateDate: 2019/7/23 12:00
	* @Version: 0.0.1
    * @param
    * @return
    */
@Data
public class CerInfoRespDTO implements Serializable {
    private static final long serialVersionUID = 6829593946835108905L;

    @ApiModelProperty("拥有者")
    private String ownwer;

    @ApiModelProperty("颁发机构")
    private String certificateAuthority;

    @ApiModelProperty("签名算法")
    private String signAlgorithm;

    @ApiModelProperty("有限期")
    private String periodOfValidity;
}
