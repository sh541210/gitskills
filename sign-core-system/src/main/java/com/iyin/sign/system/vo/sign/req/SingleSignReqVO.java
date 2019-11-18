package com.iyin.sign.system.vo.sign.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName: SingleSignReqVO.java
 * @Description: 单页签请求DTO
 * @Author: yml
 * @CreateDate: 2019/6/22
 * @UpdateUser: yml
 * @UpdateDate: 2019/6/22
 * @Version: 1.0.0
 */
@Data
public class SingleSignReqVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "签章源文件Base64")
    private String signOriginalFileStr;

    @ApiModelProperty(value = "签章页码")
    private Integer pageNumber;

    @ApiModelProperty(value = "签章X坐标值")
    private Float coordinatex;

    @ApiModelProperty(value = "签章Y坐标值")
    private Float coordinatey;

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
