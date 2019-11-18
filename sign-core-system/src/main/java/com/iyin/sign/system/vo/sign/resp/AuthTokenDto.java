package com.iyin.sign.system.vo.sign.resp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 请求token 2018/10/12 0012.
 */
@Data
@ApiModel(value = "令牌信息")
public class AuthTokenDto implements Serializable{

    @ApiModelProperty(value="接口调用凭据")
    private String accessToken;
    @ApiModelProperty(value="Token有效时间（秒）")
    private long expireTime;
    @JsonIgnore
    private String validTime;
}

