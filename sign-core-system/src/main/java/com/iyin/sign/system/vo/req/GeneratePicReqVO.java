package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class GeneratePicReqVO {

    @ApiModelProperty("企业名称-对应上环绕文字")
    @NotBlank(message = "企业名称不能为空")
    private String enterpriseName;

    @ApiModelProperty("印章名称-对应横排文字")
    private String sealName;

    @ApiModelProperty("印章中心图案 0为内部为五角星，1为单线五角星，2为党徽，3为无图案")
    @NotNull(message = "印章中心图案类型不能为空")
    private Integer centerDesign;

    @ApiModelProperty("数字编码")
    private String numberCode;
}
