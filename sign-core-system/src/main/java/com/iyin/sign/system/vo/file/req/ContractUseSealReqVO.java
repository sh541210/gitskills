package com.iyin.sign.system.vo.file.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: ContractUseSealReqVO
 * @Description: 用印申请
 * @Author: yml
 * @CreateDate: 2019/10/10
 * @UpdateUser: yml
 * @UpdateDate: 2019/10/10
 * @Version: 1.0.0
 */
@Data
public class ContractUseSealReqVO {

    /**
     * 印章设备地址
     */
    @ApiModelProperty(value = "印章id")
    @NotBlank(message = "印章id不能为空")
    private String sealId;

    /**
     * 印章名称
     */
    @ApiModelProperty(value = "印章名称")
    private String sealName;

    /**
     * 申请事由
     */
    @ApiModelProperty(value = "申请事由")
    @NotBlank(message = "申请事由不能为空")
    private String applyCause;

    /**
     * 申请次数
     */
    @ApiModelProperty(value = "申请次数")
    @NotNull(message = "申请次数不能为空")
    private Integer applyCount;

    /**
     * 失效时间
     */
    @ApiModelProperty(value = "失效时间")
    @NotNull(message = "失效时间不能为空")
    private Long expireTime;

    @ApiModelProperty(value = "用户ID",hidden = true)
    private String userId;

    @ApiModelProperty(value = "合同ID")
    @NotBlank(message = "合同ID不能为空")
    private String contractId;

    /**
     * 来源1合同2文件3用印申请
     */
    @ApiModelProperty(value = "来源1合同2文件3用印申请",hidden = true)
    @JsonIgnore
    private Integer source;

}
