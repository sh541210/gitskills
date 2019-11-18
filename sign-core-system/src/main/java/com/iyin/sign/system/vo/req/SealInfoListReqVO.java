package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @ClassName: SealInfoListReqVO
 * @Description: 查询单位下的印章列表
 * @Author: luwenxiong
 * @CreateDate: 2019/6/26 10:33
 * @UpdateUser: Administrator
 * @UpdateDate: 2019/6/26 10:33
 * @Version: 0.0.1
 */
@Data
public class SealInfoListReqVO implements Serializable {

    private static final long serialVersionUID = -3443509684430892197L;

    @ApiModelProperty(value = "单位ID")
    @NotBlank(message = "单位ID不能为空")
    private String enterpriseOrPersonalId;

    @ApiModelProperty(value = "介质类型 01：云印章 02 ukey印章")
    private String mediumType;

    private Integer pageSize;

    private Integer currentPage;
}
