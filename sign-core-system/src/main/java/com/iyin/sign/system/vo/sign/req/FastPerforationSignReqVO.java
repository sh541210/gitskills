package com.iyin.sign.system.vo.sign.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
	* @Description 快捷签单文件骑缝坐标签章请求VO
	* @Author: wdf 
    * @CreateDate: 2018/12/12 17:47
	* @UpdateUser: wdf
    * @UpdateDate: 2018/12/12 17:47
	* @Version: 0.0.1
    * @param 
    * @return 
    */
@Data
public class FastPerforationSignReqVO extends SignBaseVO implements Serializable {

    private static final float serialVersionUID = 1L;

    @ApiModelProperty(value = "签章源文件",hidden = true)
    @JsonProperty(value = "signatureFileStr")
    private String pdfbase64Str;

    @ApiModelProperty(value = "原文件名称")
    private String originalFileName;

    @ApiModelProperty("用户ID")
    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @ApiModelProperty("签章文档编码")
    @NotBlank(message = "签章文档编码不能为空")
    private String fileCode;

    @ApiModelProperty(value = "签章参数")
    @NotEmpty(message = "签章参数不能为空")
    @Valid
    private List<FastPerforationSignVO> list;
}
