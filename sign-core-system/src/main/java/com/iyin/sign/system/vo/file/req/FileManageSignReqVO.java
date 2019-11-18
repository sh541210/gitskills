package com.iyin.sign.system.vo.file.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: CompactSignReqVO
 * @Description: 签署请求
 * @Author: yml
 * @CreateDate: 2019/8/8
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/8
 * @Version: 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FileManageSignReqVO extends CompactFieldInfoReqVO{

    @ApiModelProperty(value = "原文件名称")
    private String originalFileName;

    @ApiModelProperty(value = "章模编码")
    @NotBlank(message = "章模编码不能为空")
    private String sealCode;

    @ApiModelProperty(value = "文件ID")
    private String compactId;

    @ApiModelProperty(value = "是否雾化")
    @NotNull(message = "是否雾化不能为空")
    private boolean foggy;

    @ApiModelProperty(value = "是否脱密")
    @NotNull(message = "是否脱密不能为空")
    private boolean grey;
}
