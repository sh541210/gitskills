package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName FileResourceInfoVO
 * FileResourceInfoVO
 * @Author wdf
 * @Date 2019/7/24 11:13
 * @throws
 * @Version 1.0
 **/
@Data
public class FileResourceInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "上传资源用户Id")
    @NotBlank(message = "用户Id不能为空")
    private String userId;

    @ApiModelProperty(value = "文件资源编码UUID")
    @NotBlank(message = "文件资源编码不能为空")
    private String fileCode;

    /**
     * 验证码 为0没有验证码
     */
    @ApiModelProperty(value = "验证码 为0没有验证码")
    @NotBlank(message = "验证码不能为空")
    private String verificationCode;

    /**
     * 是否启用二维码0否1是
     */
    @ApiModelProperty(value = "是否启用二维码0否1是")
    @NotNull(message = "二维码不能为空")
    private Integer qrCode;

    /**
     * 验证失效为0永久有效
     */
    @ApiModelProperty(value = "验证失效为0永久有效 否则 时间字符串格式yyyy-mm-dd")
    @NotBlank(message = "验证失效不能为空")
    private String gmtVerification;


}
