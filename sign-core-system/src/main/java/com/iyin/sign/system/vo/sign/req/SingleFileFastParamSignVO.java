package com.iyin.sign.system.vo.sign.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
	* @Description 快捷签单页签章
	* @Author: wdf 
    * @CreateDate: 2018/12/12 17:47
	* @UpdateUser: wdf
    * @UpdateDate: 2018/12/12 17:47
	* @Version: 0.0.1
    * @param 
    * @return 
    */
@Data
class SingleFileFastParamSignVO implements Serializable {

    private static final float serialVersionUID = 1L;

    @ApiModelProperty(value = "待签章原文件名称")
    private String originalFileName;

    @ApiModelProperty(value = "章模原文件名称")
    private String sealFileName;

    @ApiModelProperty(value = "证书原文件名称")
    private String certFileName;

    @ApiModelProperty(value = "印章类型 章模业务类型：01 行政章、02 财务章等 03 业务专用章 04 法定代表人名章 05 报关专用章 06 合同专用章 07 其他公章 08 电子杂章： 09 电子私章")
    @NotBlank(message = "印章类型不能为空")
    private String sealType;

    /**
     * 介质类型 01 云印章  02ukey印章
     */
    @ApiModelProperty(value = "介质类型 01 云印章  02ukey印章")
    @NotBlank(message = "介质类型不能为空")
    private String mediumType;

    @ApiModelProperty(value = "签章方式 1：关键字签章 2：坐标签章")
    @NotBlank(message = "签章方式不能为空")
    private String signatureMethod;

    /*
     *关键字
     */

    @ApiModelProperty(value = "关键字索引号")
    private String keywordIndex;

    @ApiModelProperty(value = "关键字")
    private String keyword;

    @ApiModelProperty(value = "关键字x轴偏移量")
    private String keywordOffsetX;

    @ApiModelProperty(value = "关键字y轴偏移量")
    private String keywordOffsetY;

    /*
     *坐标
     */

    @ApiModelProperty(value = "签章X坐标轴")
    private String coordinateX;

    @ApiModelProperty(value = "签章Y坐标轴")
    private String coordinateY;

    @ApiModelProperty(value = "签章页数")
    private String pageNo;
}
