package com.iyin.sign.system.dto.req;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @ClassName: TwoPressureCodeReqDTO
 * @Description: 二压码请求DTO
 * @Author: luwenxiong
 * @CreateDate: 2018/9/6 10:27
 * @UpdateUser: luwenxiong
 * @UpdateDate: 2018/9/6 10:27
 * @Version: 0.0.1
 */
@Data
public class TwoPressureCodeReqDTO implements Serializable{

    private static final Long serialVersionUID = 1L;

    @NotBlank(message = "压缩码不能为空")
    String eyCode;

    String base64;

    /**上传的图片途径，01:章模，02:手绘签名*/
    String picType;

}
