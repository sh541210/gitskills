package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: SycnUserReqVO
 * @Description: 同步用户信息—白鹤
 * @Author: yml
 * @CreateDate: 2019/10/9
 * @UpdateUser: yml
 * @UpdateDate: 2019/10/9
 * @Version: 1.0.0
 */
@Data
@ApiModel("同步用户信息—白鹤")
public class SycnUserReqVO {

    @ApiModelProperty("账号,与电话字段一致")
    @NotBlank(message = "账号不能为空")
    private String id;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("电话")
    @NotNull(message = "电话不能为空")
    private String mobilePhone;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty("用户姓名")
    @NotBlank(message = "用户姓名不能为空")
    private String userName;

}
