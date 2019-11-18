package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @ClassName: UnBindUserSealReqVO
 * @Description: 解绑用户与印章的关联
 * @Author: luwenxiong
 * @CreateDate: 2019/7/19 17:32
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/7/19 17:32
 * @Version: 0.0.1
 */
@Data
public class UnBindUserSealReqVO implements Serializable {
    private static final long serialVersionUID = -950492407227523912L;

    @ApiModelProperty(value = "印章ID")
    @NotBlank(message = "印章ID不能为空")
    private String sealId;

    @ApiModelProperty(value = "用户ID")
    @NotBlank(message = "用户ID不能为空")
    private String userId;
}
