package com.iyin.sign.system.vo.sign.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName: JudgePositionReqVO
 * @Description: 签章位置判断请求参数
 * @Author: yml
 * @CreateDate: 2019/4/26
 * @UpdateUser: yml
 * @UpdateDate: 2019/4/26
 * @Version: 1.0.0
 */
@Data
public class JudgePositionReqVO implements Serializable {

    @ApiModelProperty("当前页")
    @NotNull(message = "当前页不能为空")
    private int page;
    @ApiModelProperty("左下角X座标")
    @NotNull(message = "左下角X座标不能为空")
    private float xCoordinate;
    @ApiModelProperty("左下角Y座标")
    @NotNull(message = "左下角Y座标不能为空")
    private float yCoordinate;
    @ApiModelProperty("右下角X座标")
    @NotNull(message = "右下角X座标不能为空")
    private float wCoordinate;
    @ApiModelProperty("左上角Y座标")
    @NotNull(message = "左上角Y座标不能为空")
    private float hCoordinate;
    @ApiModelProperty("pdf文件编码")
    private String fileCode;
}
