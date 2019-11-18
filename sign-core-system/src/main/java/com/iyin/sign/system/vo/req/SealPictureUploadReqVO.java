package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName: SealPictureUploadToHtReqVO
 * @Description: 上传章莫文件
 * @Author: luwenxiong
 * @CreateDate: 2018/11/7 10:22
 * @UpdateUser: luwenxiong
 * @UpdateDate: 2018/11/7 10:22
 * @Version: 0.0.1
 */
@Data
public class SealPictureUploadReqVO implements Serializable{
    private static final Long serialVersionUID = 1L;

    @ApiModelProperty(value="文件不能为空")
    @NotNull(message = "文件不能为空")
    private MultipartFile file;

    @ApiModelProperty(value = "上传的章模类型，01:章模，02:手绘签名")
    @NotBlank(message = "章模类型不能为空")
    private String pictureType;

    @ApiModelProperty("混淆码")
    private String mixCode;

    @ApiModelProperty("章模宽")
    private Integer width;

    @ApiModelProperty("章模高")
    private Integer height;
}
