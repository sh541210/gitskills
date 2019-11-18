package com.iyin.sign.system.vo.sign.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
	* @Description 快捷签证书
	* @Author: wdf
    * @CreateDate: 2018/12/21 11:01
	* @UpdateUser: wdf
    * @UpdateDate: 2018/12/21 11:01
	* @Version: 0.0.1
    * @param
    * @return
    */
@Data
public class SignCertificateRespDTO implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "pk12证书密码")
    private String password;

    @ApiModelProperty(value = "pk12证书Base64码")
    private String dataResult;

}
