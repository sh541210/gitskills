package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @ClassName: UpdateAdminInfoReqVO
 * @Description: 修改用户信息
 * @Author: luwenxiong
 * @CreateDate: 2019/6/26 10:07
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/26 10:07
 * @Version: 0.0.1
 */
@Data
public class UpdateUserInfoReqVO implements Serializable {
    private static final long serialVersionUID = -5564013718857113672L;

    @ApiModelProperty(value = "用户ID")
    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @ApiModelProperty(value = "旧的密码")
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    @ApiModelProperty(value = "新密码")
    @NotBlank(message = "新密码不能为空")
    private String newPassword;



}
