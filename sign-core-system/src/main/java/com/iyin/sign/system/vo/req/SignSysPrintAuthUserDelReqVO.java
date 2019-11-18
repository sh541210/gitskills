package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>
 * 打印分配表
 * </p>
 *
 * @author wdf
 * @since 2019-08-06
 */
@Data
@Accessors(chain = true)
@ApiModel(value ="删除已分配对象")
public class SignSysPrintAuthUserDelReqVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件编码
     */
    @ApiModelProperty(value = "文件编码")
    @NotBlank(message = "文件编码不能为空")
    private String fileCode;

    /**
     * 用户ID列表，逗号分隔
     */
    @ApiModelProperty(value = "用户ID列表，逗号分隔")
    @NotBlank(message = "用户列表不能为空")
    private String userIds;
}
