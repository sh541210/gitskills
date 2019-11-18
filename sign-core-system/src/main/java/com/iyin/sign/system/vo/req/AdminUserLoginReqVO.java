package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @ClassName: AdminUserLoginReqVO
 * @Description: AdminUserLoginReqVO
 * @Author: luwenxiong
 * @CreateDate: 2019/6/20 15:39
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/20 15:39
 * @Version: 0.0.1
 */
@Data
public class AdminUserLoginReqVO  implements Serializable {

    @NotBlank(message = "用户名不能为空")
    private String userName;

    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String valicode;
}
