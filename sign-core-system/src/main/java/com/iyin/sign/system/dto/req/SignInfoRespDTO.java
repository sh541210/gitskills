package com.iyin.sign.system.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
	* 签名信息
	* @Author: wdf 
    * @CreateDate: 2019/7/23 12:00
	* @UpdateUser: wdf
    * @UpdateDate: 2019/7/23 12:00
	* @Version: 0.0.1
    * @param 
    * @return 
    */
@Data
public class SignInfoRespDTO implements Serializable {
    private static final long serialVersionUID = -122017629992395001L;

    @ApiModelProperty(value = "时间戳")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date timestamp;

    @ApiModelProperty(value = "是否有效")
    private String effectiveDes;

    @ApiModelProperty(value = "签署时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date signTime;

    @ApiModelProperty(value = "是否被篡改")
    private String updateDes;

    @ApiModelProperty(value = "时间戳认证")
    private String timesVerify;

}
