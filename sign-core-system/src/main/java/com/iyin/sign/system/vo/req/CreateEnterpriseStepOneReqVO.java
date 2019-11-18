package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName: CreateEnterpriseStepOneReqVO
 * @Description: 单位管理-新建单位信息-1
 * @Author: luwenxiong
 * @CreateDate: 2019/6/27 10:32
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/27 10:32
 * @Version: 0.0.1
 */
@Data
public class CreateEnterpriseStepOneReqVO implements Serializable {
    private static final long serialVersionUID = 4122566222547116737L;

    @ApiModelProperty(value = "单位名称")
    @NotBlank(message = "单位名称不能为空")
    private String chineseName;

    @ApiModelProperty(value = "企业信用代码")
    @NotBlank(message = "企业信用代码不能为空")
    private String creditCode;

    @ApiModelProperty(value = "法人名称")
    @NotBlank(message = "法人名称不能为空")
    @Length(max = 30,message = "法人姓名不能超过30个字符")
    private String legalName;

    @ApiModelProperty(value = "自定义字段")
    private String extDefine;
}
