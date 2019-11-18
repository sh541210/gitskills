package com.iyin.sign.system.vo.sign.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName: PerforationSignReqVO
 * @Description: 骑缝签请求DTO
 * @Author: 唐德繁
 * @CreateDate: 2018/11/27 0027 下午 6:31
 * @UpdateUser: 唐德繁
 * @UpdateDate: 2018/11/27 0027 下午 6:31
 * @Version: 0.0.1
 */
@Data
public class PerforationSignReqVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "签章源文件Base64")
    private String signOriginalFileStr;

    @ApiModelProperty(value = "起始签章页码")
    private Integer startPageNumber;

    @ApiModelProperty(value = "结束签章页码")
    private Integer endPageNumber;

    @ApiModelProperty(value = "签章Y坐标值")
    private Float coordinateY;

    @ApiModelProperty(value = "签章方向（0：表示左；1：表示右；）")
    @NotBlank(message = "签章方向不能为空")
    private String signatureDirection;

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

    @ApiModelProperty(value = "覆盖页面")
    private String pageSize;

}