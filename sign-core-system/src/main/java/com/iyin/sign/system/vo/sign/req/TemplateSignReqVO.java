package com.iyin.sign.system.vo.sign.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
	* @Description 快捷签单页签章请求VO
	* @Author: wdf 
    * @CreateDate: 2018/12/12 17:47
	* @UpdateUser: wdf
    * @UpdateDate: 2018/12/12 17:47
	* @Version: 0.0.1
    * @param 
    * @return 
    */
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplateSignReqVO extends SignBaseVO implements Serializable {

    private static final float serialVersionUID = 1L;

    @ApiModelProperty(value = "签章源文件",hidden = true)
    @JsonProperty(value = "signatureFileStr")
    private String pdfBase64Str;

    @ApiModelProperty(value = "原文件名称")
    private String originalFileName;

    @ApiModelProperty("签章文档编码")
    @NotBlank(message = "签章文档编码不能为空")
    private String fileCode;

    @ApiModelProperty("用户ID")
    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @ApiModelProperty("印章编码，多个逗号分隔")
    @NotBlank(message = "印章编码不能为空")
    private String sealCodes;

    @ApiModelProperty("模版ID")
    @NotBlank(message = "模版ID不能为空")
    private String templateId;
}
