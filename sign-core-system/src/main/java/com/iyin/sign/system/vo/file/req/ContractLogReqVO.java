package com.iyin.sign.system.vo.file.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: ContractLogReqVO
 * @Description: 合同操作日志请求
 * @Author: yml
 * @CreateDate: 2019/8/30
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/30
 * @Version: 1.0.0
 */
@Data
public class ContractLogReqVO {

    @ApiModelProperty(value = "用户ID",hidden = true)
    private String userId;

    @ApiModelProperty("合同ID")
    @NotBlank(message = "合同ID不能为空")
    private String contractId;

    @ApiModelProperty("页码")
    @NotNull(message = "页码不能为空")
    private Integer pageNum;

    @ApiModelProperty("页数")
    @NotNull(message = "页数不能为空")
    private Integer pageSize;
}
