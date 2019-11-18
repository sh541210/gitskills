package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @ClassName: SealPictureReqVO
 * @Description: 上传章莫文件
 * @Author: luwenxiong
 * @CreateDate: 2018/11/7 10:22
 * @UpdateUser: luwenxiong
 * @UpdateDate: 2018/11/7 10:22
 * @Version: 0.0.1
 */
@Data
public class SealPictureReqVO implements Serializable{

    private static final long serialVersionUID = -633384417948913097L;

    @ApiModelProperty("上传的章模类型，01:章模，02:手绘签名")
    @NotBlank(message = "章模图片类型不能为空")
    private String pictureType;

    @ApiModelProperty("混淆码")
    private String mixCode;

    @ApiModelProperty("章模宽")
    private Integer width;

    @ApiModelProperty("章模高")
    private Integer height;
}
