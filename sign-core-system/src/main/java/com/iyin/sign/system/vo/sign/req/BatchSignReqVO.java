package com.iyin.sign.system.vo.sign.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName: BatchSignReqVO
 * @Description: 多页签章请求DTO
 * @Author: 唐德繁
 * @CreateDate: 2018/11/27 0027 下午 6:22
 * @UpdateUser: 唐德繁
 * @UpdateDate: 2018/11/27 0027 下午 6:22
 * @Version: 0.0.1
 */
@Data
public class BatchSignReqVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "签章源文件Base64")
    private String signOriginalFileStr;

    @ApiModelProperty(value = "起始签章页码")
    private Integer startPageNumber;

    @ApiModelProperty(value = "结束签章页码")
    private Integer endPageNumber;

    @ApiModelProperty(value = "签章X坐标值")
    private Float coordinateX;

    @ApiModelProperty(value = "签章Y坐标值")
    private Float coordinateY;

    @ApiModelProperty(value = "章模文件Base64")
    private String sealStr;

    @ApiModelProperty(value = "证书文件Base64")
    private String certStr;

    @ApiModelProperty(value = "证书密钥")
    private String certPassword;

    @ApiModelProperty(value = "印章编码")
    private String sealCode;

    @ApiModelProperty(value = "是否雾化")
    @NotNull(message = "是否雾化不能为空")
    private boolean foggy;

    @ApiModelProperty(value = "是否脱密")
    @NotNull(message = "是否脱密不能为空")
    private boolean grey;

}
