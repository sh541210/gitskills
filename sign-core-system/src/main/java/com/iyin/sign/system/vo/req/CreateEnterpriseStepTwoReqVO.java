package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @ClassName: CreateEnterpriseStepOneReqVO
 * @Description: 单位管理-新建单位信息-2
 * @Author: luwenxiong
 * @CreateDate: 2019/6/27 10:32
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/27 10:32
 * @Version: 0.0.1
 */
@Data
public class CreateEnterpriseStepTwoReqVO implements Serializable {
    private static final long serialVersionUID = 4122566222547116737L;

    @ApiModelProperty(value = "第一步返回的单位ID")
    @NotBlank(message = "单位ID不能为空")
    private String enterpriseId;

    @ApiModelProperty(value = "用户名 可以是手机号 或邮箱")
    @NotBlank(message = "用户名不能为空")
    private String userName;

    @ApiModelProperty(value = "用户名类型 01 手机号  02 邮箱")
    @NotBlank(message = "用户名类型不能为空")
    private String userNameType;

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;
}
