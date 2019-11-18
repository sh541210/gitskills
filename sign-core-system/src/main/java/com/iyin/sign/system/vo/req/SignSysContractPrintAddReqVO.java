package com.iyin.sign.system.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName: SignSysContractPrintAddReqVO.java
 * @Description: 合同打印分配
 * @Author: yml
 * @CreateDate: 2019/9/4
 * @UpdateUser: yml
 * @UpdateDate: 2019/9/4
 * @Version: 1.0.0
 */
@Data
@Accessors(chain = true)
@ApiModel(value ="添加合同打印分配")
public class SignSysContractPrintAddReqVO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 文件编码
     */
    @ApiModelProperty(value = "合同ID")
    @NotBlank(message = "合同ID不能为空")
    private String contractId;

    /**
     * 用户ID列表，逗号分隔
     */
    @ApiModelProperty(value = "用户ID列表，逗号分隔")
    @NotBlank(message = "用户列表不能为空")
    private String userIds;

    /**
     * 是否雾化 0否 1是
     */
    @ApiModelProperty(value = "是否雾化 0否 1是")
    @Value("0")
    @Range(min=0,max = 1)
    private Integer isFoggy;

    /**
     * 是否脱密 0否 1是
     */
    @Value("0")
    @Range(min=0,max = 1)
    @ApiModelProperty(value = "是否脱密 0否 1是")
    private Integer isGrey;

    /**
     * 打印次数
     */
    @ApiModelProperty(value = "打印次数")
    @Range(min = 1)
    @NotNull(message = "打印次数不允许为空")
    private Long printNum;
}
