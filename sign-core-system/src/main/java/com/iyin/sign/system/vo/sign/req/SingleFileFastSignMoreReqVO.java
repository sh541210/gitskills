package com.iyin.sign.system.vo.sign.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
	* @Description 快捷签单页多签章请求VO
	* @Author: wdf 
    * @CreateDate: 2018/12/12 17:47
	* @UpdateUser: wdf
    * @UpdateDate: 2018/12/12 17:47
	* @Version: 0.0.1
    * @param 
    * @return 
    */
@Data
@EqualsAndHashCode(callSuper = true)
public class SingleFileFastSignMoreReqVO extends SingleFileFastCoordBatchSignVO implements Serializable {

    private static final float serialVersionUID = 1L;

    @ApiModelProperty(value = "签章源文件")
    @JsonProperty(value = "signatureFileStr")
    private String signatureFileStr;

    @ApiModelProperty(value = "章模源文件")
    @JsonProperty(value = "sealBase64Str")
    private String sealBase64Str;

    @ApiModelProperty(value = "证书源文件")
    @JsonProperty(value = "certBase64Str")
    private String certBase64Str;

    @ApiModelProperty(value = "证书密钥")
    @NotBlank(message = "证书密钥不能为空")
    private String certPassword;

    @ApiModelProperty("用户ID")
    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @ApiModelProperty(value = "是否雾化")
    @NotNull(message = "是否雾化不能为空")
    private boolean foggy;

    @ApiModelProperty(value = "是否脱密")
    @NotNull(message = "是否脱密不能为空")
    private boolean grey;

    @ApiModelProperty("入参文件类型设置 0文件code,1 base64")
    @Value("0")
    private int inType;

    @ApiModelProperty("出参文件类型设置 0文件code,1 base64")
    @Value("0")
    private int outType;

}
