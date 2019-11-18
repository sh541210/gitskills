package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName: ForbidEnterpriseReqVO
 * @Description: 启用禁用单面请求VO
 * @Author: luwenxiong
 * @CreateDate: 2019/7/19 17:46
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/19 17:46
 * @Version: 0.0.1
 */
@Data
public class ForbidEnterpriseReqVO implements Serializable {
    private static final long serialVersionUID = -4331906675696560814L;

    @ApiModelProperty(value = "单位ID")
    @NotBlank(message = "单位ID不能为空")
    private String enterpriseId;

    @ApiModelProperty(value = "0:启用 1：禁用")
    @NotNull(message = "未设置启用或禁用")
    private Integer forbid;
}
