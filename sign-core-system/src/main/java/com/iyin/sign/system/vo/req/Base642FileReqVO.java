package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @ClassName: Base64ToFileReqVO
 * @Description:
 * @Author: yml
 * @CreateDate: 2019/3/29
 * @UpdateUser: yml
 * @UpdateDate: 2019/3/29
 * @Version: 1.0.0
 */
@Data
public class Base642FileReqVO implements Serializable {

    @ApiModelProperty("文件base64")
    @NotBlank(message = "文件BASE64不能为空")
    private String fileBase64;
}
