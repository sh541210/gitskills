package com.iyin.sign.system.vo.sign.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: UKeySignReqVO
 * @Description: uKey签章
 * @Author: yml
 * @CreateDate: 2019/7/31
 * @UpdateUser: yml
 * @UpdateDate: 2019/7/31
 * @Version: 1.0.0
 */
@Data
public class UKeySignReqVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("uKey签章的pdf文件base64")
    @NotBlank(message = "uKey签章的pdf文件base64不能为空")
    private String uKeySignPdfBase64;

    @ApiModelProperty("uKey签章的用户")
    @NotBlank(message = "uKey签章的用户不能为空")
    private String userId;

    @ApiModelProperty("签章文件编码")
    @NotBlank(message = "签章文件文件编码不能为空")
    private String fileCode;

    @ApiModelProperty("签章文件名称")
    @NotBlank(message = "签章文件名称不能为空")
    private String fileName;

    @ApiModelProperty(value = "签章参数")
    @NotEmpty(message = "签章参数不能为空")
    @Valid
    private List<UKeySignBaseVO> list;
}
