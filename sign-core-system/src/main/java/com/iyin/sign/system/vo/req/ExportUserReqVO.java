package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: ExportEnterpriseReqVO
 * @Description: 导入用户数据
 * @Author: luwenxiong
 * @CreateDate: 2019/7/17 16:35
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/17 16:35
 * @Version: 0.0.1
 */
@Data
public class ExportUserReqVO implements Serializable {

    private static final long serialVersionUID = 3768700836224346700L;

    @ApiModelProperty(value = "行num")
    private Integer lineNum;

    @ApiModelProperty(value = "用户昵称")
    private String userNickName;

    @ApiModelProperty(value = "用户名(手机号或邮箱)")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String password;
}
