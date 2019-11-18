package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 模板表
 * </p>
 *
 * @author wdf
 * @since 2019-07-15
 */
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
@ApiModel(value = "模板信息列表入参")
public class SignSysTemplateListVO implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * 模板编号列表
     */
    @ApiModelProperty(value = "模板编号列表")
    private List<String> userIds;

    /**
     * 模板名称
     */
    @ApiModelProperty(value = "模板名称")
    private String tempName;

}
