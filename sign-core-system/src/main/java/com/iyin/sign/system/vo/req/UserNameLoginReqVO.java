package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @ClassName: UserUserNameLoginReqVO
 * @Description: 用户名登录请求vo
 * @Author: luwenxiong
 * @CreateDate: 2019/6/21 18:04
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/21 18:04
 * @Version: 0.0.1
 */
@Data
public class UserNameLoginReqVO implements Serializable {

    @ApiModelProperty(value = "账号", required = true)
    @NotBlank(message = "账号不能为空")
    private String userName;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "验证码",required = true)
    @NotBlank(message = "验证码不能为空")
    private String valicode;


}
