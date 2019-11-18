package com.iyin.sign.system.vo.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @ClassName TokenReqVO
 * @Desscription: token
 * @Author wdf
 * @Date 2018/12/24 15:14
 * @Version 1.0
 **/
@Data
public class TokenReqVO implements Serializable {

    @ApiModelProperty(value = "第三方用户唯一凭证AppID")
    @NotBlank(message = "client_id不能为空")
    @JsonProperty(value = "client_id")
    private String clientId;
    @ApiModelProperty(value = "第三方用户唯一凭证密钥AppSecret")
    @NotBlank(message = "凭证密钥不能为空")
    @JsonProperty(value = "client_secret")
    private String clientSecret;
    @ApiModelProperty(value = "获取access_token填写client_credentials")
    @NotBlank(message = "grant_type不能为空")
    @JsonProperty(value = "grant_type")
    private String grantType;
    @ApiModelProperty(value = "read")
    @NotBlank(message = "scope不能为空")
    private String scope;
    @ApiModelProperty(value = "对应refresh_token")
    @JsonProperty(value = "refresh_token")
    private String refreshToken;
}
