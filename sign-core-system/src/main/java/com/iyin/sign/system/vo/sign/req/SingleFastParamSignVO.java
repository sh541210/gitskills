package com.iyin.sign.system.vo.sign.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
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
public class SingleFastParamSignVO implements Serializable {

    private static final float serialVersionUID = 1L;


    @ApiModelProperty(value = "章模编码")
    @NotBlank(message = "章模编码不能为空")
    private String sealCode;

    @ApiModelProperty(value = "是否雾化")
    @NotNull(message = "是否雾化不能为空")
    private boolean foggy;

    @ApiModelProperty(value = "是否脱密")
    @NotNull(message = "是否脱密不能为空")
    private boolean grey;

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
