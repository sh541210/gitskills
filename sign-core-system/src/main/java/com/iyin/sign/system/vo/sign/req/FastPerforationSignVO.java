package com.iyin.sign.system.vo.sign.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
	* @Description 快捷签单文件骑缝坐标签章请求
	* @Author: wdf 
    * @CreateDate: 2018/12/12 17:47
	* @UpdateUser: wdf
    * @UpdateDate: 2018/12/12 17:47
	* @Version: 0.0.1
    * @param 
    * @return 
    */
@Data
public class FastPerforationSignVO implements Serializable {

	@ApiModelProperty(value = "章模编码")
	@NotBlank(message = "章模编码不能为空")
	private String sealCode;

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

	@ApiModelProperty(value = "覆盖页面")
	private String pageSize;

	@ApiModelProperty(value = "是否雾化")
	@NotNull(message = "是否雾化不能为空")
	private boolean foggy;

	@ApiModelProperty(value = "是否脱密")
	@NotNull(message = "是否脱密不能为空")
	private boolean grey;

}
