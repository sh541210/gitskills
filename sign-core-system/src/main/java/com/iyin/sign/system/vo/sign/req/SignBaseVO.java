package com.iyin.sign.system.vo.sign.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName SignBaseVO
 * @Desscription: token
 * @Author wdf
 * @Date 2018/12/20 10:07
 * @Version 1.0
 **/
@Data
class SignBaseVO implements Serializable {

    @ApiModelProperty(value = "接口调用凭据",hidden = true)
    private String token;
}
