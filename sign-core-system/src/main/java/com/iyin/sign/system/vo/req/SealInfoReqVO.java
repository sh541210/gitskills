package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @ClassName FileResourceInfoVO
 * FileResourceInfoVO
 * @Author wdf
 * @Date 2019/7/24 11:13
 * @throws
 * @Version 1.0
 **/
@Data
public class SealInfoReqVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "人员Id")
    @NotBlank(message = "人员Id不能为空")
    private String userId;

    @ApiModelProperty(value = "印章设备地址")
    @NotBlank(message = "印章设备地址不能为空")
    private String mac;

    /**
     * 印章名称
     */
    @ApiModelProperty(value = "印章名称")
    @NotBlank(message = "印章名称不能为空")
    private String name;

    /**
     * 部门
     */
    @ApiModelProperty(value = "部门")
    private String departmentId;

    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用")
    private Boolean enableFlag;

}
