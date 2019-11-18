package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @ClassName: UpdateEnterpriseReqVO
 * @Description: 修改单位信息
 * @Author: luwenxiong
 * @CreateDate: 2019/7/1 15:11
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/1 15:11
 * @Version: 0.0.1
 */
@Data
public class UpdateEnterpriseReqVO implements Serializable {
    private static final long serialVersionUID = -1747278574141336950L;

    @NotBlank(message = "单位Id不能为空")
    private String enterpriseId;

    @NotBlank(message = "单位名称不能为空")
    private String chineseName;

    @NotBlank(message = "企业信用代码不能为空")
    private String creditCode;

    @NotBlank(message = "法人名称不能为空")
    private String legalName;

    @ApiModelProperty(value = "自定义字段")
    private String extDefine;

}
