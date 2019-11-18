package com.iyin.sign.system.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * @ClassName TokenRespVO
 * @Desscription: 创建token返回信息
 * @Author wdf
 * @Date 2018/12/24 15:19
 * @Version 1.0
 **/
@Data
public class TokenRespVO implements Serializable {

    @ApiModelProperty(value = "调用应用token")
    private String access_token;

    @ApiModelProperty(value = "更新令牌")
    private String refresh_token;

    @ApiModelProperty(value = "令牌类型")
    private String token_type;

    @ApiModelProperty(value = "Token有效时间（秒）")
    private String expires_in;

    public Integer getExpires_in(){
        return StringUtils.isNotBlank(this.expires_in) && Integer.parseInt(this.expires_in) > 1000 ? Integer.parseInt(this.expires_in)/1000 : 0;
    }
}
