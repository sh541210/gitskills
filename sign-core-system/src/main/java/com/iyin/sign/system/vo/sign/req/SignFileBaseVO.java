package com.iyin.sign.system.vo.sign.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @ClassName SignBaseVO
 * @Desscription: token
 * @Author wdf
 * @Date 2018/12/20 10:07
 * @Version 1.0
 **/
@Data
class SignFileBaseVO implements Serializable {

    @ApiModelProperty("接口调用凭据")
    @NotBlank(message = "接口调用凭据不能为空")
    private String token;
}
