package com.iyin.sign.system.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 打印分配
 * </p>
 *
 * @author wdf
 * @since 2019-08-06
 */
@Data
@Accessors(chain = true)
@ApiModel(value ="打印分配")
public class SignSysPrintAuthUserRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private String userId;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称")
    private String userNickName;
}
