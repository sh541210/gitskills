package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: ExportEnterpriseReqVO
 * @Description: 导入单位数据
 * @Author: luwenxiong
 * @CreateDate: 2019/7/17 16:35
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/17 16:35
 * @Version: 0.0.1
 */
@Data
public class ExportEnterpriseReqVO implements Serializable {

    private static final long serialVersionUID = 3768700836224346700L;

    @ApiModelProperty(value = "行num")
    private Integer lineNum;

    @ApiModelProperty(value = "单位名称")
    private String enterpriseName;

    @ApiModelProperty(value = "统一社会信用代码")
    private String creditCode;

    @ApiModelProperty(value = "法人名称")
    private String legalName;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String password;
}
