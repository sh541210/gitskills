package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName: ForbidUserReqVO
 * @Description: 禁用或启用用户
 * @Author: luwenxiong
 * @CreateDate: 2019/7/18 11:36
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/18 11:36
 * @Version: 0.0.1
 */
@Data
public class ForbidUserReqVO implements Serializable {
    private static final long serialVersionUID = -4865624274661540204L;

    @ApiModelProperty(value = "用户ID")
    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @ApiModelProperty(value = "0:启用  1：禁用")
    @NotNull(message = "forbid不能为空")
    private Integer forbid;
}
