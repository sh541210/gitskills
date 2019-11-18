package com.iyin.sign.system.vo.sign.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
	* @Description 快捷签单文件骑缝连页坐标签章请求
	* @Author: wdf 
    * @CreateDate: 2018/12/12 17:47
	* @UpdateUser: wdf
    * @UpdateDate: 2018/12/12 17:47
	* @Version: 0.0.1
    * @param 
    * @return 
    */
@Data
public class FastFilePerforationHalfSignVO implements Serializable {

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

	@ApiModelProperty(value = "签章Y坐标轴")
	@NotBlank(message = "签章Y坐标轴不能为空")
	private String coordinateY;

	@ApiModelProperty(value = "起始签章页数")
	@NotBlank(message = "起始签章页数不能为空")
	private String startPageNo;

	@ApiModelProperty(value = "结束签章页数")
	@NotBlank(message = "结束签章页数不能为空")
	private String endPageNo;

	@ApiModelProperty(value = "签章方向0：表示左 1：表示右")
	@NotBlank(message = "签章方向不能为空")
	private String signatureDirection;

}
